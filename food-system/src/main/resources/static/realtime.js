// Real-time menu updates for admin menu-management page
if (window.location.pathname.includes('/admin/menu')) {
    const socket = new SockJS('/websocket');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/menu-updates', function (message) {
            // Reload the page or update the menu table dynamically
            location.reload();
        });
    });
}

// Real-time user updates for admin user-management page
if (window.location.pathname.includes('/admin/users')) {
    const socket = new SockJS('/websocket');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/user-updates', function (message) {
            location.reload();
        });
    });
}

// Real-time order updates for admin orders page
if (window.location.pathname.includes('/admin/orders')) {
    const socket = new SockJS('/websocket');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/order-updates', function (message) {
            location.reload();
        });
    });
}
