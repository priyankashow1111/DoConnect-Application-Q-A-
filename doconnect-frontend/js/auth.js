// ===== AUTH UTILITIES =====
// Token and user info stored in localStorage after login

const saveUserSession = (data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('username', data.username);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('role', data.role);
};

const saveAdminSession = (data) => {
    localStorage.setItem('adminToken', data.token);
    localStorage.setItem('adminUsername', data.username);
    localStorage.setItem('adminId', data.adminId);
};

const isLoggedIn = () => !!localStorage.getItem('token');

const isAdminLoggedIn = () => !!localStorage.getItem('adminToken');

const logout = () => {
    localStorage.clear();
    window.location.href = 'login.html';
};

const adminLogout = () => {
    localStorage.removeItem('adminToken');
    localStorage.removeItem('adminUsername');
    localStorage.removeItem('adminId');
    window.location.href = 'admin-login.html';
};

// Redirect to login if not authenticated
const requireLogin = () => {
    if (!isLoggedIn()) window.location.href = 'login.html';
};

const requireAdminLogin = () => {
    if (!isAdminLoggedIn()) window.location.href = 'admin-login.html';
};
