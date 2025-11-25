// ========================================
// NEXUS COMMAND - CYBER DASHBOARD ENGINE
// MILLION DOLLAR INTERACTIVITY
// ========================================

class CyberDashboard {
    constructor() {
        this.isInitialized = false;
        this.realTimeData = {};
        this.animations = {};
        this.websocket = null;
        this.init();
    }

    init() {
        if (this.isInitialized) return;
        
        console.log('🚀 Initializing Nexus Command Dashboard...');
        
        this.setupEventListeners();
        this.init3DBackground();
        this.startRealTimeUpdates();
        this.initDataCounters();
        this.setupWebSocket();
        this.initCharts();
        
        this.isInitialized = true;
        console.log('✅ Nexus Command Dashboard Ready');
    }

    // ========== 3D BACKGROUND SYSTEM ==========
    init3DBackground() {
        const canvas = document.getElementById('cyberBackground');
        if (!canvas) return;

        try {
            const scene = new THREE.Scene();
            const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
            const renderer = new THREE.WebGLRenderer({ 
                canvas: canvas,
                alpha: true,
                antialias: true 
            });

            renderer.setSize(window.innerWidth, window.innerHeight);
            renderer.setClearColor(0x000000, 0);

            // Create cyber grid
            this.createCyberGrid(scene);
            
            // Add floating particles
            this.createFloatingParticles(scene);

            camera.position.z = 15;

            // Animation loop
            const animate = () => {
                requestAnimationFrame(animate);
                
                // Rotate grid slowly
                if (scene.getObjectByName('cyberGrid')) {
                    scene.getObjectByName('cyberGrid').rotation.x += 0.001;
                    scene.getObjectByName('cyberGrid').rotation.y += 0.001;
                }

                // Animate particles
                this.animateParticles(scene);

                renderer.render(scene, camera);
            };

            animate();

            // Handle resize
            window.addEventListener('resize', () => {
                camera.aspect = window.innerWidth / window.innerHeight;
                camera.updateProjectionMatrix();
                renderer.setSize(window.innerWidth, window.innerHeight);
            });

        } catch (error) {
            console.warn('3D background not available:', error);
        }
    }

    createCyberGrid(scene) {
        const gridGroup = new THREE.Group();
        gridGroup.name = 'cyberGrid';

        const gridSize = 20;
        const gridDivisions = 20;
        const gridHelper = new THREE.GridHelper(gridSize, gridDivisions, 0x00f3ff, 0x00aaff);
        gridHelper.material.opacity = 0.1;
        gridHelper.material.transparent = true;
        gridGroup.add(gridHelper);

        // Add floating cubes
        for (let i = 0; i < 50; i++) {
            const size = Math.random() * 0.3 + 0.1;
            const geometry = new THREE.BoxGeometry(size, size, size);
            const material = new THREE.MeshBasicMaterial({
                color: new THREE.Color().setHSL(Math.random(), 0.8, 0.6),
                wireframe: true,
                transparent: true,
                opacity: 0.3
            });

            const cube = new THREE.Mesh(geometry, material);
            cube.position.set(
                (Math.random() - 0.5) * 15,
                (Math.random() - 0.5) * 15,
                (Math.random() - 0.5) * 15
            );
            
            cube.rotation.set(
                Math.random() * Math.PI,
                Math.random() * Math.PI,
                Math.random() * Math.PI
            );

            gridGroup.add(cube);
        }

        scene.add(gridGroup);
    }

    createFloatingParticles(scene) {
        const particleCount = 100;
        const particles = new THREE.BufferGeometry();
        const positions = new Float32Array(particleCount * 3);
        const colors = new Float32Array(particleCount * 3);

        for (let i = 0; i < particleCount; i++) {
            const i3 = i * 3;
            positions[i3] = (Math.random() - 0.5) * 30;
            positions[i3 + 1] = (Math.random() - 0.5) * 30;
            positions[i3 + 2] = (Math.random() - 0.5) * 30;

            const color = new THREE.Color();
            color.setHSL(Math.random(), 0.8, 0.6);
            colors[i3] = color.r;
            colors[i3 + 1] = color.g;
            colors[i3 + 2] = color.b;
        }

        particles.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        particles.setAttribute('color', new THREE.BufferAttribute(colors, 3));

        const particleMaterial = new THREE.PointsMaterial({
            size: 0.1,
            vertexColors: true,
            transparent: true,
            opacity: 0.6
        });

        const particleSystem = new THREE.Points(particles, particleMaterial);
        particleSystem.name = 'floatingParticles';
        scene.add(particleSystem);
    }

    animateParticles(scene) {
        const particleSystem = scene.getObjectByName('floatingParticles');
        if (!particleSystem) return;

        const positions = particleSystem.geometry.attributes.position.array;

        for (let i = 0; i < positions.length; i += 3) {
            positions[i] += (Math.random() - 0.5) * 0.02;
            positions[i + 1] += (Math.random() - 0.5) * 0.02;
            positions[i + 2] += (Math.random() - 0.5) * 0.02;

            // Wrap around boundaries
            if (Math.abs(positions[i]) > 15) positions[i] *= -0.95;
            if (Math.abs(positions[i + 1]) > 15) positions[i + 1] *= -0.95;
            if (Math.abs(positions[i + 2]) > 15) positions[i + 2] *= -0.95;
        }

        particleSystem.geometry.attributes.position.needsUpdate = true;
    }

    // ========== REAL-TIME DATA UPDATES ==========
    startRealTimeUpdates() {
        // Simulate real-time data updates
        setInterval(() => {
            this.updateMetrics();
            this.updateOrderStream();
            this.updateAIRecommendations();
        }, 3000);

        // Initial update
        this.updateMetrics();
    }

    updateMetrics() {
        const metrics = {
            activeOrders: Math.floor(Math.random() * 50) + 1200,
            menuItems: Math.floor(Math.random() * 10) + 335,
            todayRevenue: Math.floor(Math.random() * 500) + 12000,
            aiScore: (Math.random() * 5 + 95).toFixed(1)
        };

        this.animateCounter('activeOrders', metrics.activeOrders);
        this.animateCounter('menuItems', metrics.menuItems);
        this.animateCounter('todayRevenue', metrics.todayRevenue);
        this.animateCounter('aiScore', metrics.aiScore);
    }

    animateCounter(elementId, targetValue) {
        const element = document.getElementById(elementId);
        if (!element) return;

        const currentValue = parseFloat(element.textContent.replace('$', '')) || 0;
        const duration = 1000;
        const startTime = performance.now();
        const startValue = currentValue;

        const animate = (currentTime) => {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);
            
            // Easing function
            const easeOutQuart = 1 - Math.pow(1 - progress, 4);
            const current = startValue + (targetValue - startValue) * easeOutQuart;

            if (elementId === 'aiScore') {
                element.textContent = current.toFixed(1);
            } else if (elementId === 'todayRevenue') {
                element.textContent = `$${Math.floor(current).toLocaleString()}`;
            } else {
                element.textContent = Math.floor(current).toLocaleString();
            }

            if (progress < 1) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }

    updateOrderStream() {
        const streamContainer = document.getElementById('liveOrdersStream');
        if (!streamContainer) return;

        const orders = [
            { id: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`, location: 'New York', amount: `$${(Math.random() * 50 + 20).toFixed(2)}`, time: 'Just now' },
            { id: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`, location: 'London', amount: `$${(Math.random() * 80 + 30).toFixed(2)}`, time: '1 min ago' },
            { id: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`, location: 'Tokyo', amount: `$${(Math.random() * 60 + 25).toFixed(2)}`, time: '2 mins ago' }
        ];

        orders.forEach(order => {
            const orderElement = document.createElement('div');
            orderElement.className = 'stream-item new-order';
            orderElement.innerHTML = `
                <span class="order-id">${order.id}</span>
                <span class="order-location">${order.location}</span>
                <span class="order-amount">${order.amount}</span>
                <span class="order-time">${order.time}</span>
            `;
            
            orderElement.style.animation = 'fadeInUp 0.5s ease-out';
            streamContainer.insertBefore(orderElement, streamContainer.firstChild);

            // Remove old orders
            if (streamContainer.children.length > 8) {
                streamContainer.removeChild(streamContainer.lastChild);
            }
        });
    }

    updateAIRecommendations() {
        const recommendations = [
            {
                priority: 'high',
                icon: 'fas fa-fire',
                title: 'TRENDING NOW',
                content: 'Spicy Dragon Roll trending +42% this hour',
                metric: '+$1,240 potential'
            },
            {
                priority: 'medium', 
                icon: 'fas fa-chart-line',
                title: 'PRICE OPTIMIZATION',
                content: 'Increase Truffle Fries by 15% for max profit',
                metric: '87% confidence'
            },
            {
                priority: 'critical',
                icon: 'fas fa-exclamation-triangle', 
                title: 'STOCK ALERT',
                content: 'Avocado stock critical - 3.2 hours remaining',
                metric: 'URGENT'
            }
        ];

        const grid = document.querySelector('.recommendations-grid');
        if (!grid) return;

        grid.innerHTML = '';
        
        recommendations.forEach(rec => {
            const card = document.createElement('div');
            card.className = `recommendation-card`;
            card.setAttribute('data-priority', rec.priority);
            card.innerHTML = `
                <div class="rec-icon">
                    <i class="${rec.icon}"></i>
                </div>
                <div class="rec-content">
                    <h5>${rec.title}</h5>
                    <p>${rec.content}</p>
                    <div class="rec-metrics">
                        <span class="metric-tag">${rec.metric}</span>
                    </div>
                </div>
                <button class="rec-action">${rec.priority === 'critical' ? 'ORDER NOW' : 'OPTIMIZE'}</button>
            `;
            
            card.style.animation = 'fadeInUp 0.6s ease-out';
            grid.appendChild(card);
        });
    }

    // ========== CHART INITIALIZATION ==========
    initCharts() {
        this.initSentimentChart();
        this.initPerformanceCharts();
    }

    initSentimentChart() {
        const ctx = document.getElementById('sentimentRadar');
        if (!ctx) return;

        new Chart(ctx, {
            type: 'radar',
            data: {
                labels: ['Taste', 'Service', 'Speed', 'Value', 'Innovation', 'Experience'],
                datasets: [{
                    label: 'Customer Satisfaction',
                    data: [92, 88, 95, 85, 90, 87],
                    backgroundColor: 'rgba(0, 243, 255, 0.2)',
                    borderColor: '#00f3ff',
                    borderWidth: 2,
                    pointBackgroundColor: '#00f3ff',
                    pointBorderColor: '#ffffff',
                    pointHoverBackgroundColor: '#ffffff',
                    pointHoverBorderColor: '#00f3ff'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    r: {
                        angleLines: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        },
                        pointLabels: {
                            color: '#ffffff',
                            font: {
                                family: "'Exo 2', sans-serif"
                            }
                        },
                        ticks: {
                            backdropColor: 'transparent',
                            color: 'rgba(255, 255, 255, 0.6)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        labels: {
                            color: '#ffffff',
                            font: {
                                family: "'Exo 2', sans-serif"
                            }
                        }
                    }
                }
            }
        });
    }

    initPerformanceCharts() {
        // Additional chart initializations would go here
        console.log('📊 Performance charts initialized');
    }

    // ========== WEBSOCKET CONNECTION ==========
    setupWebSocket() {
        // Simulate WebSocket connection for real-time data
        console.log('🔌 Establishing Nexus WebSocket connection...');
        
        // In a real implementation, this would connect to your backend
        setTimeout(() => {
            console.log('✅ WebSocket connected - Real-time data streaming active');
            this.simulateWebSocketData();
        }, 2000);
    }

    simulateWebSocketData() {
        // Simulate incoming WebSocket data
        setInterval(() => {
            const eventType = ['new_order', 'order_update', 'system_alert'][Math.floor(Math.random() * 3)];
            const data = {
                type: eventType,
                timestamp: new Date().toISOString(),
                payload: this.generateWebSocketPayload(eventType)
            };
            
            this.handleWebSocketMessage(data);
        }, 5000);
    }

    generateWebSocketPayload(eventType) {
        switch (eventType) {
            case 'new_order':
                return {
                    orderId: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`,
                    amount: (Math.random() * 100 + 20).toFixed(2),
                    location: ['New York', 'London', 'Tokyo', 'Paris'][Math.floor(Math.random() * 4)],
                    items: Math.floor(Math.random() * 5) + 1
                };
            case 'order_update':
                return {
                    orderId: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`,
                    status: ['preparing', 'ready', 'delivered'][Math.floor(Math.random() * 3)],
                    updateTime: new Date().toLocaleTimeString()
                };
            case 'system_alert':
                return {
                    level: ['info', 'warning', 'critical'][Math.floor(Math.random() * 3)],
                    message: 'System performance optimal',
                    timestamp: new Date().toISOString()
                };
        }
    }

    handleWebSocketMessage(data) {
        console.log('📡 WebSocket Message:', data);
        
        // Handle different message types
        switch (data.type) {
            case 'new_order':
                this.handleNewOrder(data.payload);
                break;
            case 'order_update':
                this.handleOrderUpdate(data.payload);
                break;
            case 'system_alert':
                this.handleSystemAlert(data.payload);
                break;
        }
    }

    handleNewOrder(payload) {
        this.showNotification(`New Order: ${payload.orderId} - $${payload.amount}`, 'success');
        this.updateMetrics();
    }

    handleOrderUpdate(payload) {
        this.showNotification(`Order ${payload.orderId} updated to: ${payload.status}`, 'info');
    }

    handleSystemAlert(payload) {
        if (payload.level === 'critical') {
            this.showNotification(`SYSTEM ALERT: ${payload.message}`, 'error');
        }
    }

    // ========== NOTIFICATION SYSTEM ==========
    showNotification(message, type = 'info') {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `cyber-notification ${type}`;
        notification.innerHTML = `
            <div class="notification-content">
                <i class="fas fa-${this.getNotificationIcon(type)}"></i>
                <span>${message}</span>
            </div>
            <button class="notification-close">&times;</button>
        `;

        // Add styles
        notification.style.cssText = `
            position: fixed;
            top: 100px;
            right: 20px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 10px;
            padding: 15px 20px;
            color: white;
            font-family: 'Exo 2', sans-serif;
            z-index: 10000;
            display: flex;
            align-items: center;
            gap: 10px;
            max-width: 400px;
            animation: slideInRight 0.3s ease-out;
            border-left: 4px solid ${this.getNotificationColor(type)};
        `;

        // Add close button functionality
        const closeBtn = notification.querySelector('.notification-close');
        closeBtn.onclick = () => notification.remove();

        document.body.appendChild(notification);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.style.animation = 'slideOutRight 0.3s ease-in';
                setTimeout(() => notification.remove(), 300);
            }
        }, 5000);
    }

    getNotificationIcon(type) {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-triangle',
            warning: 'exclamation-circle',
            info: 'info-circle'
        };
        return icons[type] || 'info-circle';
    }

    getNotificationColor(type) {
        const colors = {
            success: '#00ff41',
            error: '#ff0080', 
            warning: '#ff6b35',
            info: '#00f3ff'
        };
        return colors[type] || '#00f3ff';
    }

    // ========== EVENT LISTENERS ==========
    setupEventListeners() {
        // Navigation
        document.addEventListener('click', (e) => {
            if (e.target.matches('.portal-btn')) {
                this.handleNavigation(e.target);
            }
            
            if (e.target.matches('.rec-action')) {
                this.handleRecommendationAction(e.target);
            }
            
            if (e.target.matches('.orb-action')) {
                this.handleOrbAction(e.target);
            }
        });

        // Keyboard shortcuts
        document.addEventListener('keydown', (e) => {
            this.handleKeyboardShortcuts(e);
        });

        // Quick actions orb
        const quickActions = document.getElementById('quickActions');
        if (quickActions) {
            quickActions.addEventListener('click', () => {
                this.toggleQuickActions();
            });
        }
    }

    handleNavigation(button) {
        const section = button.getAttribute('data-section');
        console.log(`🔄 Navigating to: ${section}`);
        
        // Update active state
        document.querySelectorAll('.portal-btn').forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
        
        // Add navigation effects
        button.style.transform = 'scale(0.95)';
        setTimeout(() => {
            button.style.transform = 'scale(1)';
        }, 150);
    }

    handleRecommendationAction(button) {
        const card = button.closest('.recommendation-card');
        const priority = card.getAttribute('data-priority');
        
        console.log(`🎯 Action taken on ${priority} priority recommendation`);
        
        // Add click effect
        button.style.transform = 'scale(0.9)';
        setTimeout(() => {
            button.style.transform = 'scale(1)';
        }, 150);
        
        this.showNotification('Recommendation action executed', 'success');
    }

    handleOrbAction(button) {
        const action = button.getAttribute('data-action');
        console.log(`⚡ Quick action: ${action}`);
        
        // Handle different orb actions
        switch (action) {
            case 'emergency-stock':
                this.showNotification('Emergency stock order placed', 'info');
                break;
            case 'ai-optimize':
                this.showNotification('AI optimization initiated', 'success');
                break;
            case 'system-scan':
                this.showNotification('System security scan running', 'warning');
                break;
        }
    }

    handleKeyboardShortcuts(e) {
        // Ctrl + 1-4 for quick navigation
        if (e.ctrlKey && e.key >= '1' && e.key <= '4') {
            e.preventDefault();
            const sections = ['dashboard', 'menu', 'orders', 'users'];
            const section = sections[parseInt(e.key) - 1];
            this.navigateToSection(section);
        }
        
        // Space for quick actions
        if (e.code === 'Space' && !e.target.matches('input, textarea')) {
            e.preventDefault();
            this.toggleQuickActions();
        }
    }

    navigateToSection(section) {
        const button = document.querySelector(`[data-section="${section}"]`);
        if (button) {
            button.click();
        }
    }

    toggleQuickActions() {
        const orb = document.querySelector('.action-orb');
        orb.classList.toggle('expanded');
    }

    // ========== UTILITY METHODS ==========
    formatCurrency(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    }

    formatNumber(number) {
        return new Intl.NumberFormat('en-US').format(number);
    }

    // ========== PERFORMANCE OPTIMIZATION ==========
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        }
    }
}

// ========== INITIALIZATION ==========
function initCyberDashboard() {
    // Wait for DOM to be fully loaded
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            window.cyberDashboard = new CyberDashboard();
        });
    } else {
        window.cyberDashboard = new CyberDashboard();
    }
}

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { CyberDashboard, initCyberDashboard };
} else {
    // Initialize immediately
    initCyberDashboard();
}

// ========== GLOBAL UTILITIES ==========
window.formatCyberNumber = (number) => {
    return new Intl.NumberFormat('en-US').format(number);
};

window.formatCyberCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
};

window.createCyberGlow = (element, color = '#00f3ff') => {
    element.style.boxShadow = `0 0 20px ${color}, 0 0 40px ${color}40`;
};

window.removeCyberGlow = (element) => {
    element.style.boxShadow = '';
};