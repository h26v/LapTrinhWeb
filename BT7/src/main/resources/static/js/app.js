(function ($) {
    'use strict';

    const contextPath = window.APP_CONTEXT || '';
    const state = {
        resource: 'products',
        page: 0,
        size: 8,
        search: '',
        categoryId: '',
        editingId: null,
        categories: []
    };

    const copy = {
        products: {
            title: 'Products',
            subtitle: 'Quản lý danh mục sản phẩm qua RESTful API và AJAX.',
            add: 'Thêm product',
            table: 'Product directory',
            endpoint: '/api/product'
        },
        categories: {
            title: 'Categories',
            subtitle: 'Tổ chức nhóm sản phẩm và theo dõi dữ liệu liên quan.',
            add: 'Thêm category',
            table: 'Category directory',
            endpoint: '/api/category'
        }
    };

    const modal = new bootstrap.Modal(document.getElementById('entity-modal'));
    let searchTimer;

    function escapeHtml(value) {
        return $('<div>').text(value == null ? '' : value).html();
    }

    function formatMoney(value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'USD' }).format(Number(value || 0));
    }

    function api(endpoint, options) {
        return $.ajax($.extend({
            url: contextPath + endpoint,
            dataType: 'json'
        }, options || {})).then(function (response) {
            if (!response.success) {
                return $.Deferred().reject({ responseJSON: response }).promise();
            }
            return response.data;
        });
    }

    function showToast(message, type) {
        const toast = $('#app-toast');
        toast.removeClass('success error').addClass(type || 'success');
        $('#toast-message').text(message);
        bootstrap.Toast.getOrCreateInstance(toast[0], { delay: 3000 }).show();
    }

    function errorMessage(xhr) {
        return xhr && xhr.responseJSON && xhr.responseJSON.message
            ? xhr.responseJSON.message : 'Không thể hoàn tất thao tác. Vui lòng thử lại.';
    }

    function setLoading(loading) {
        $('#loading-state').toggleClass('d-none', !loading);
        $('.data-table').toggleClass('invisible', loading);
        if (loading) $('#empty-state').addClass('d-none');
    }

    function loadCategories() {
        return api('/api/category?page=0&size=50&search=').then(function (data) {
            state.categories = data.items || [];
            $('#category-nav-count').text(data.totalItems);
            const filter = $('#category-filter');
            const current = state.categoryId;
            filter.html('<option value="">Tất cả category</option>');
            state.categories.forEach(function (category) {
                filter.append($('<option>', { value: category.categoryId, text: category.categoryName }));
            });
            filter.val(current);
        });
    }

    function loadData() {
        const c = copy[state.resource];
        const query = new URLSearchParams({ page: state.page, size: state.size, search: state.search });
        if (state.resource === 'products' && state.categoryId) query.set('categoryId', state.categoryId);
        setLoading(true);
        return api(c.endpoint + '?' + query.toString()).then(function (data) {
            render(data);
        }).catch(function (xhr) {
            showToast(errorMessage(xhr), 'error');
        }).always(function () {
            setLoading(false);
        });
    }

    function render(data) {
        const items = data.items || [];
        $('#total-records').text(data.totalItems);
        $('#visible-records').text(items.length);
        $('#page-indicator').text('Trang ' + (data.totalPages ? data.page + 1 : 0) + '/' + data.totalPages);
        $('#footer-summary').text(data.totalItems ? 'Hiển thị ' + (data.page * data.size + 1) + '–' + (data.page * data.size + items.length) + ' trong ' + data.totalItems : 'Không có bản ghi');
        if (state.resource === 'products') $('#product-nav-count').text(data.totalItems);
        if (state.resource === 'categories') $('#category-nav-count').text(data.totalItems);
        renderHead();
        renderBody(items);
        renderPagination(data);
        $('#empty-state').toggleClass('d-none', items.length > 0);
    }

    function renderHead() {
        const head = state.resource === 'products'
            ? '<tr><th>ID</th><th>Product</th><th>Category</th><th>Đơn giá</th><th>Giảm</th><th>Kho</th><th>Trạng thái</th><th class="text-end">Actions</th></tr>'
            : '<tr><th>ID</th><th>Icon</th><th>Category name</th><th>Products</th><th class="text-end">Actions</th></tr>';
        $('#table-head').html(head);
    }

    function imageMarkup(url, className, fallbackIcon) {
        if (!url) return '<span class="' + className + ' placeholder"><i class="' + fallbackIcon + '"></i></span>';
        return '<img class="' + className + '" data-fallback-icon="' + escapeHtml(fallbackIcon) + '" src="' + escapeHtml(url) + '" alt="" loading="lazy">';
    }

    function renderBody(items) {
        const body = $('#table-body');
        body.empty();
        items.forEach(function (item) {
            if (state.resource === 'products') {
                const statusClass = Number(item.status) === 1 ? 'status-active' : 'status-inactive';
                const statusText = Number(item.status) === 1 ? 'Đang bán' : 'Tạm dừng';
                body.append('<tr>' +
                    '<td class="id-cell">#' + escapeHtml(item.productId) + '</td>' +
                    '<td><div class="product-cell">' + imageMarkup(item.images, 'product-thumb', 'fa-solid fa-cube') + '<div><div class="product-name">' + escapeHtml(item.productName) + '</div><div class="product-subtitle">SKU-' + String(item.productId).padStart(4, '0') + '</div></div></div></td>' +
                    '<td><span class="category-chip">' + escapeHtml(item.categoryName) + '</span></td>' +
                    '<td class="price-cell">' + formatMoney(item.unitPrice) + '</td>' +
                    '<td class="discount-cell">' + escapeHtml(Number(item.discount || 0).toFixed(0)) + '%</td>' +
                    '<td class="quantity-cell">' + escapeHtml(item.quantity) + '</td>' +
                    '<td><span class="status-badge ' + statusClass + '">' + statusText + '</span></td>' +
                    actionMarkup(item.productId) +
                    '</tr>');
            } else {
                body.append('<tr>' +
                    '<td class="id-cell">#' + escapeHtml(item.categoryId) + '</td>' +
                    '<td>' + imageMarkup(item.icon, 'category-icon', 'fa-solid fa-layer-group') + '</td>' +
                    '<td><div class="product-name">' + escapeHtml(item.categoryName) + '</div><div class="product-subtitle">Category group</div></td>' +
                    '<td class="quantity-cell">' + escapeHtml(item.productCount) + ' product' + (Number(item.productCount) === 1 ? '' : 's') + '</td>' +
                    actionMarkup(item.categoryId) +
                    '</tr>');
            }
        });
        body.find('img[data-fallback-icon]').on('error', function () {
            const image = $(this);
            image.replaceWith('<span class="' + image.attr('class') + ' placeholder"><i class="' + escapeHtml(image.data('fallback-icon')) + '"></i></span>');
        });
    }

    function actionMarkup(id) {
        return '<td><div class="action-group"><button type="button" class="action-button edit" data-id="' + escapeHtml(id) + '" title="Sửa"><i class="fa-solid fa-pen"></i></button><button type="button" class="action-button delete" data-id="' + escapeHtml(id) + '" title="Xóa"><i class="fa-regular fa-trash-can"></i></button></div></td>';
    }

    function renderPagination(data) {
        const pagination = $('#pagination').empty();
        const totalPages = data.totalPages || 0;
        if (totalPages <= 1) return;
        pagination.append(pageButton('<i class="fa-solid fa-chevron-left"></i>', data.page - 1, data.page === 0));
        const pages = pageNumbers(data.page, totalPages);
        pages.forEach(function (page) {
            if (page === '...') pagination.append('<li class="page-dots">…</li>');
            else pagination.append(pageButton(page + 1, page, false, page === data.page));
        });
        pagination.append(pageButton('<i class="fa-solid fa-chevron-right"></i>', data.page + 1, data.page >= totalPages - 1));
    }

    function pageButton(label, page, disabled, active) {
        return $('<li>', { class: 'page-item' + (disabled ? ' disabled' : '') + (active ? ' active' : '') })
            .append($('<button>', { class: 'page-link', type: 'button', html: label, 'data-page': page }));
    }

    function pageNumbers(current, total) {
        if (total <= 5) return Array.from({ length: total }, (_, i) => i);
        if (current <= 2) return [0, 1, 2, '...', total - 1];
        if (current >= total - 3) return [0, '...', total - 3, total - 2, total - 1];
        return [0, '...', current, '...', total - 1];
    }

    function switchResource(resource) {
        state.resource = resource;
        state.page = 0;
        state.search = '';
        state.categoryId = '';
        const c = copy[resource];
        $('.side-link').removeClass('active').filter('[data-resource="' + resource + '"]').addClass('active');
        $('#breadcrumb-current, #page-title').text(c.title);
        $('#page-subtitle').text(c.subtitle);
        $('#add-label').text(c.add);
        $('#table-title').text(c.table);
        $('#endpoint-label').text(c.endpoint);
        $('#search-input').val('').attr('placeholder', resource === 'products' ? 'Tìm theo tên product...' : 'Tìm theo tên category...');
        $('#category-filter').toggleClass('d-none', resource !== 'products');
        loadData();
    }

    function fieldsFor(resource, item) {
        item = item || {};
        if (resource === 'categories') {
            return '<div class="row g-3"><div class="col-12"><label class="form-label" for="categoryName">Tên category <span class="text-danger">*</span></label><input class="form-control" id="categoryName" name="categoryName" required maxlength="100" value="' + escapeHtml(item.categoryName) + '" placeholder="Ví dụ: Electronics"></div><div class="col-md-7"><label class="form-label" for="iconUrl">Icon URL</label><input class="form-control" id="iconUrl" name="iconUrl" maxlength="500" value="' + escapeHtml(item.icon) + '" placeholder="https://..."></div><div class="col-md-5"><label class="form-label" for="icon-file">Hoặc upload icon</label><input class="form-control" id="icon-file" name="icon" type="file" accept="image/*"><div class="form-text">File upload sẽ được lưu trong thư mục uploads.</div></div></div>';
        }
        const categoryOptions = state.categories.map(function (category) {
            return '<option value="' + category.categoryId + '" ' + (String(category.categoryId) === String(item.categoryId) ? 'selected' : '') + '>' + escapeHtml(category.categoryName) + '</option>';
        }).join('');
        return '<div class="row g-3"><div class="col-md-8"><label class="form-label" for="productName">Tên product <span class="text-danger">*</span></label><input class="form-control" id="productName" name="productName" required maxlength="150" value="' + escapeHtml(item.productName) + '" placeholder="Ví dụ: Wireless Keyboard"></div><div class="col-md-4"><label class="form-label" for="categoryId">Category <span class="text-danger">*</span></label><select class="form-select" id="categoryId" name="categoryId" required><option value="">Chọn category</option>' + categoryOptions + '</select></div><div class="col-md-6"><label class="form-label" for="unitPrice">Đơn giá (USD) <span class="text-danger">*</span></label><input class="form-control" id="unitPrice" name="unitPrice" required type="number" min="0" step="0.01" value="' + escapeHtml(item.unitPrice) + '"></div><div class="col-md-3"><label class="form-label" for="discount">Giảm giá (%)</label><input class="form-control" id="discount" name="discount" required type="number" min="0" max="100" step="0.01" value="' + escapeHtml(item.discount == null ? 0 : item.discount) + '"></div><div class="col-md-3"><label class="form-label" for="quantity">Số lượng <span class="text-danger">*</span></label><input class="form-control" id="quantity" name="quantity" required type="number" min="0" step="1" value="' + escapeHtml(item.quantity == null ? 0 : item.quantity) + '"></div><div class="col-md-7"><label class="form-label" for="images">Image URL</label><input class="form-control" id="images" name="images" maxlength="500" value="' + escapeHtml(item.images) + '" placeholder="https://..."></div><div class="col-md-5"><label class="form-label" for="imageFile">Hoặc upload hình</label><input class="form-control" id="imageFile" name="imageFile" type="file" accept="image/*"></div><div class="col-12"><label class="form-label" for="description">Mô tả</label><textarea class="form-control" id="description" name="description" rows="3" maxlength="2000" placeholder="Mô tả ngắn về product">' + escapeHtml(item.description) + '</textarea></div><div class="col-12"><label class="form-label" for="status">Trạng thái</label><select class="form-select" id="status" name="status"><option value="1" ' + (Number(item.status == null ? 1 : item.status) === 1 ? 'selected' : '') + '>Đang bán</option><option value="0" ' + (Number(item.status) === 0 ? 'selected' : '') + '>Tạm dừng</option></select></div></div>';
    }

    function openForm(item) {
        state.editingId = item ? (state.resource === 'products' ? item.productId : item.categoryId) : null;
        $('#modal-kicker').text(state.editingId ? 'EDIT RECORD' : 'NEW RECORD');
        $('#modal-title').text(state.editingId ? 'Cập nhật ' + (state.resource === 'products' ? 'product' : 'category') : copy[state.resource].add);
        $('#form-fields').html(fieldsFor(state.resource, item));
        modal.show();
    }

    function getItem(id) {
        return api(copy[state.resource].endpoint + '/' + id).then(function (item) { openForm(item); }).catch(function (xhr) { showToast(errorMessage(xhr), 'error'); });
    }

    function formPayload() {
        const form = $('#entity-form')[0];
        const raw = Object.fromEntries(new FormData(form).entries());
        if (state.resource === 'categories') return { categoryName: raw.categoryName, icon: raw.iconUrl };
        return { productName: raw.productName, images: raw.images, unitPrice: Number(raw.unitPrice), discount: Number(raw.discount || 0), description: raw.description, categoryId: Number(raw.categoryId), quantity: Number(raw.quantity), status: Number(raw.status) };
    }

    function multipartPayload() {
        const form = $('#entity-form')[0];
        const raw = Object.fromEntries(new FormData(form).entries());
        const data = new FormData();
        if (state.resource === 'categories') {
            data.append('categoryName', raw.categoryName);
            if (raw.icon && raw.icon.size > 0) data.append('icon', raw.icon);
        } else {
            data.append('productName', raw.productName);
            data.append('unitPrice', raw.unitPrice);
            data.append('discount', raw.discount || '0');
            data.append('description', raw.description || '');
            data.append('categoryId', raw.categoryId);
            data.append('quantity', raw.quantity);
            data.append('status', raw.status);
            if (raw.imageFile && raw.imageFile.size > 0) data.append('imageFile', raw.imageFile);
        }
        return data;
    }

    function save() {
        const endpoint = copy[state.resource].endpoint + (state.editingId ? '/' + state.editingId : '');
        const raw = Object.fromEntries(new FormData($('#entity-form')[0]).entries());
        const hasUpload = state.resource === 'categories' ? raw.icon && raw.icon.size > 0 : raw.imageFile && raw.imageFile.size > 0;
        const requestOptions = { type: state.editingId ? 'PUT' : 'POST', data: hasUpload ? multipartPayload() : JSON.stringify(formPayload()) };
        if (hasUpload) { requestOptions.contentType = false; requestOptions.processData = false; }
        else requestOptions.contentType = 'application/json';
        const request = api(endpoint, requestOptions);
        $('#save-btn').prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-1"></span> Đang lưu...');
        request.then(function (data) {
            modal.hide();
            showToast(state.editingId ? 'Cập nhật thành công.' : 'Thêm mới thành công.');
            if (state.resource === 'categories') loadCategories().then(loadData); else loadData();
        }).catch(function (xhr) { showToast(errorMessage(xhr), 'error'); }).always(function () { $('#save-btn').prop('disabled', false).html('<i class="fa-solid fa-check"></i> Lưu thay đổi'); });
    }

    function remove(id) {
        const label = state.resource === 'products' ? 'product' : 'category';
        if (!window.confirm('Bạn có chắc muốn xóa ' + label + ' này không?')) return;
        api(copy[state.resource].endpoint + '/' + id, { type: 'DELETE' }).then(function () {
            showToast('Đã xóa ' + label + ' thành công.');
            if (state.resource === 'categories') loadCategories().then(loadData); else loadData();
        }).catch(function (xhr) { showToast(errorMessage(xhr), 'error'); });
    }

    $('.side-link').on('click', function () { switchResource($(this).data('resource')); });
    $('#refresh-btn').on('click', function () { loadData(); });
    $('#add-btn').on('click', function () { openForm(); });
    $('#search-input').on('input', function () {
        state.search = $(this).val().trim();
        state.page = 0;
        clearTimeout(searchTimer);
        searchTimer = setTimeout(loadData, 300);
    });
    $('#category-filter').on('change', function () { state.categoryId = $(this).val(); state.page = 0; loadData(); });
    $('#page-size').on('change', function () { state.size = Number($(this).val()); state.page = 0; loadData(); });
    $('#pagination').on('click', '[data-page]', function () { if (!$(this).parent().hasClass('disabled')) { state.page = Number($(this).data('page')); loadData(); } });
    $('#table-body').on('click', '.edit', function () { getItem($(this).data('id')); });
    $('#table-body').on('click', '.delete', function () { remove($(this).data('id')); });
    $('#entity-form').on('submit', function (event) { event.preventDefault(); if (this.checkValidity()) save(); else this.classList.add('was-validated'); });
    $(document).on('keydown', function (event) { if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') { event.preventDefault(); $('#search-input').trigger('focus'); } });
    $('#entity-modal').on('hidden.bs.modal', function () { $('#entity-form').removeClass('was-validated'); });

    loadCategories().then(loadData).catch(function (xhr) { showToast(errorMessage(xhr), 'error'); });
})(jQuery);
