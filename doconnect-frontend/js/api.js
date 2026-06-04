// Base URL - all requests go through API Gateway
const BASE_URL = 'http://localhost:8765';

// Get token from localStorage
const getToken = () => localStorage.getItem('token');
const getAdminToken = () => localStorage.getItem('adminToken');

// Common headers for authenticated requests
const authHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`
});

const adminAuthHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getAdminToken()}`
});

// ===== AUTH APIs =====
const registerUser = async (data) => {
    const res = await fetch(`${BASE_URL}/api/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    const text = await res.text();
    try {
        return JSON.parse(text);
    } catch (e) {
        return { message: text };
    }
};

const loginUser = async (data) => {
    const res = await fetch(`${BASE_URL}/api/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
};

const loginAdmin = async (data) => {
    const res = await fetch(`${BASE_URL}/api/admin/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
};

const registerAdmin = async (data) => {
    const res = await fetch(`${BASE_URL}/api/admin/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
};

// ===== QUESTION APIs =====
const getAllQuestions = async () => {
    const res = await fetch(`${BASE_URL}/api/questions/all`, {
        headers: authHeaders()
    });
    return res.json();
};

const searchQuestions = async (keyword) => {
    const res = await fetch(
        `${BASE_URL}/api/questions/search?keyword=${keyword}`,
        { headers: authHeaders() }
    );
    return res.json();
};

const getQuestionById = async (id) => {
    const res = await fetch(`${BASE_URL}/api/questions/${id}`, {
        headers: authHeaders()
    });
    return res.json();
};

const getMyQuestions = async () => {
    const res = await fetch(`${BASE_URL}/api/questions/my-questions`, {
        headers: authHeaders()
    });
    return res.json();
};

const askQuestion = async (data) => {
    const res = await fetch(`${BASE_URL}/api/questions/ask`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify(data)
    });
    return res.json();
};

// ===== ANSWER APIs =====
const getAnswers = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/answers/question/${questionId}`,
        { headers: authHeaders() }
    );
    return res.json();
};

const postAnswer = async (questionId, data) => {
    const res = await fetch(
        `${BASE_URL}/api/answers/post/${questionId}`,
        {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify(data)
        }
    );
    return res.json();
};

const getMyPendingAnswer = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/answers/my-pending/${questionId}`,
        { headers: authHeaders() }
    );
    if (res.status === 204) return null;
    return res.json();
};

// ===== LIKE APIs =====
const toggleLike = async (answerId) => {
        const res = await fetch(
        `${BASE_URL}/api/likes/toggle/${answerId}`,
        { method: 'POST', headers: authHeaders() }
    );
    return res.text();
};

const getLikeCount = async (answerId) => {
    const res = await fetch(
        `${BASE_URL}/api/likes/count/${answerId}`,
        { headers: authHeaders() }
    );
    return res.json();
};

const hasUserLiked = async (answerId) => {
    const res = await fetch(
        `${BASE_URL}/api/likes/check/${answerId}`,
        { headers: authHeaders() }
    );
    return res.json();
};

// ===== COMMENT APIs =====
const getComments = async (answerId) => {
    const res = await fetch(
        `${BASE_URL}/api/comments/answer/${answerId}`,
        { headers: authHeaders() }
    );
    return res.json();
};

const addComment = async (answerId, data) => {
    const res = await fetch(
        `${BASE_URL}/api/comments/add/${answerId}`,
        {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify(data)
        }
    );
    return res.json();
};

// ===== CHAT APIs =====
const sendMessage = async (data) => {
    const res = await fetch(`${BASE_URL}/api/chat/send`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify(data)
    });
    return res.json();
};

const getConversation = async (userId1, userId2) => {
    const res = await fetch(
        `${BASE_URL}/api/chat/conversation/${userId1}/${userId2}`,
        { headers: authHeaders() }
    );
    return res.json();
};

const getUnreadCount = async () => {
    const res = await fetch(`${BASE_URL}/api/chat/unread/count`, {
        headers: authHeaders()
    });
    return res.json();
};

const getUnreadMessages = async () => {
    const res = await fetch(`${BASE_URL}/api/chat/unread`, {
        headers: authHeaders()
    });
    return res.json();
};

const markConversationAsRead = async (senderId) => {
    await fetch(`${BASE_URL}/api/chat/read/conversation/${senderId}`, {
        method: 'PUT',
        headers: authHeaders()
    });
};

const getAllUsers = async () => {
    const res = await fetch(`${BASE_URL}/api/users/all`, {
        headers: authHeaders()
    });
    return res.json();
};

// ===== ADMIN APIs =====
const adminGetAllUsers = async () => {
    const res = await fetch(`${BASE_URL}/api/admin/users`, {
        headers: adminAuthHeaders()
    });
    return res.json();
};

const adminGetAllQuestions = async () => {
    const res = await fetch(`${BASE_URL}/api/admin/questions`, {
        headers: adminAuthHeaders()
    });
    return res.json();
};

const adminGetUnapprovedQuestions = async () => {
    const res = await fetch(
        `${BASE_URL}/api/admin/questions/unapproved`,
        { headers: adminAuthHeaders() }
    );
    return res.json();
};

const adminApproveQuestion = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/questions/approve/${questionId}`,
        {
            method: 'PUT',
            headers: adminAuthHeaders(),
            body: JSON.stringify({ remarks: 'Approved' })
        }
    );
    return res.json();
};

const adminRejectQuestion = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/questions/reject/${questionId}`,
        {
            method: 'PUT',
            headers: adminAuthHeaders(),
            body: JSON.stringify({ remarks: 'Rejected' })
        }
    );
    return res.json();
};

const adminDeleteQuestion = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/questions/delete/${questionId}`,
        { method: 'DELETE', headers: adminAuthHeaders() }
    );
    return res.text();
};

const adminCloseQuestion = async (questionId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/questions/close/${questionId}`,
        { method: 'PUT', headers: adminAuthHeaders() }
    );
    return res.text();
};

const adminGetAllAnswers = async () => {
    const res = await fetch(
        `${BASE_URL}/api/admin/answers/all`,
        { headers: adminAuthHeaders() }
    );
    return res.json();
};

const adminGetUnapprovedAnswers = async () => {
    const res = await fetch(
        `${BASE_URL}/api/admin/answers/unapproved`,
        { headers: adminAuthHeaders() }
    );
    return res.json();
};

const adminApproveAnswer = async (answerId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/answers/approve/${answerId}`,
        {
            method: 'PUT',
            headers: adminAuthHeaders(),
            body: JSON.stringify({ remarks: 'Approved' })
        }
    );
    return res.json();
};

const adminRejectAnswer = async (answerId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/answers/reject/${answerId}`,
        {
            method: 'PUT',
            headers: adminAuthHeaders(),
            body: JSON.stringify({ remarks: 'Rejected' })
        }
    );
    return res.json();
};

const adminDeactivateUser = async (userId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/users/deactivate/${userId}`,
        { method: 'PUT', headers: adminAuthHeaders() }
    );
    return res.text();
};

const adminActivateUser = async (userId) => {
    const res = await fetch(
        `${BASE_URL}/api/admin/users/activate/${userId}`,
        { method: 'PUT', headers: adminAuthHeaders() }
    );
    return res.text();
};