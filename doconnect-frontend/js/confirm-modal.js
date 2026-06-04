// Reusable confirmation modal
// Usage: showConfirm('Your message?', () => { /* confirmed action */ });

function showConfirm(message, onConfirm) {
    document.getElementById('confirmMessage').textContent = message;
    const overlay = document.getElementById('confirmOverlay');
    overlay.classList.add('active');

    const confirmBtn = document.getElementById('confirmOkBtn');
    const cancelBtn = document.getElementById('confirmCancelBtn');

    function close() {
        overlay.classList.remove('active');
        confirmBtn.removeEventListener('click', handleConfirm);
        cancelBtn.removeEventListener('click', close);
        overlay.removeEventListener('click', handleOverlayClick);
    }

    function handleConfirm() {
        close();
        onConfirm();
    }

    function handleOverlayClick(e) {
        if (e.target === overlay) close();
    }

    confirmBtn.addEventListener('click', handleConfirm);
    cancelBtn.addEventListener('click', close);
    overlay.addEventListener('click', handleOverlayClick);
}

function showToast(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    document.body.appendChild(toast);
    requestAnimationFrame(() => toast.classList.add('show'));
    setTimeout(() => {
        toast.classList.remove('show');
        toast.addEventListener('transitionend', () => toast.remove());
    }, 3000);
}
