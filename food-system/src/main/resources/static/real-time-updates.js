// ========================================
// REAL-TIME DATA STREAMING ENGINE
// LIVE UPDATES & WEBSOCKET INTEGRATION
// ========================================

class RealTimeEngine {
    constructor() {
        this.connections = new Map();
        this.dataStreams = new Map();
        this.updateCallbacks = new Map();
        this.init();
    }

    init() {
        this.setupDataStreams();
        this.startLiveUpdates();
        this.setupPerformanceMonitoring();
    }

    setupDataStreams() {
        // Order stream
        this.dataStreams.set('orders', {
            interval: 2000,
            callback: this.updateOrderStream.bind(this),
            active: true
        });

        // Metrics stream
        this.dataStreams.set('metrics', {
            interval: 3000, 
            callback: this.updateMetrics.bind(this),
            active: true
        });

        // AI recommendations stream
        this.dataStreams.set('ai_recommendations', {
            interval: 5000,
            callback: this.updateAIRecommendations.bind(this),
            active: true
        });

        // Customer activity stream
        this.dataStreams.set('customer_activity', {
            interval: 4000,
            callback: this.updateCustomerActivity.bind(this),
            active: true
        });
    }

    startLiveUpdates() {
        this.dataStreams.forEach((stream, name) => {
            if (stream.active) {
                stream.intervalId = setInterval(stream.callback, stream.interval);
            }
        });
    }

    updateOrderStream() {
        const newOrders = this.generateMockOrders();
        this.emit('orders:update', newOrders);
        
        // Update UI if elements exist
        this.updateOrderUI(newOrders);
    }

    updateMetrics() {
        const metrics = {
            activeOrders: Math.floor(Math.random() * 50) + 1200,
            completionRate: (Math.random() * 5 + 95).toFixed(1),
            revenue: Math.floor(Math.random() * 1000) + 12000,
            customerSatisfaction: (Math.random() * 3 + 94).toFixed(1)
        };

        this.emit('metrics:update', metrics);
        this.updateMetricsUI(metrics);
    }

    updateAIRecommendations() {
        const recommendations = this.generateAIRecommendations();
        this.emit('ai:recommendations', recommendations);
        this.updateRecommendationsUI(recommendations);
    }

    updateCustomerActivity() {
        const activity = this.generateCustomerActivity();
        this.emit('customers:activity', activity);
        this.updateActivityUI(activity);
    }

    // UI Update methods
    updateOrderUI(orders) {
        const streamContainer = document.getElementById('liveOrdersStream');
        if (!streamContainer) return;

        orders.forEach(order => {
            const orderElement = this.createOrderElement(order);
            streamContainer.insertBefore(orderElement, streamContainer.firstChild);

            // Limit displayed orders
            if (streamContainer.children.length > 10) {
                streamContainer.removeChild(streamContainer.lastChild);
            }
        });
    }

    updateMetricsUI(metrics) {
        // Update metric counters with animation
        Object.keys(metrics).forEach(metric => {
            const element = document.getElementById(metric);
            if (element) {
                this.animateValue(element, metrics[metric]);
            }
        });
    }

    updateRecommendationsUI(recommendations) {
        const grid = document.querySelector('.recommendations-grid');
        if (!grid) return;

        grid.innerHTML = '';
        recommendations.forEach(rec => {
            const card = this.createRecommendationCard(rec);
            grid.appendChild(card);
        });
    }

    updateActivityUI(activity) {
        const activityStream = document.querySelector('.reviews-stream');
        if (!activityStream) return;

        activity.forEach(activity => {
            const activityElement = this.createActivityElement(activity);
            activityStream.insertBefore(activityElement, activityStream.firstChild);

            if (activityStream.children.length > 8) {
                activityStream.removeChild(activityStream.lastChild);
            }
        });
    }

    // Element creation methods
    createOrderElement(order) {
        const element = document.createElement('div');
        element.className = `stream-item new-order`;
        element.innerHTML = `
            <span class="order-id">${order.id}</span>
            <span class="order-location">${order.location}</span>
            <span class="order-amount">${order.amount}</span>
            <span class="order-time">${order.time}</span>
        `;
        element.style.animation = 'fadeInUp 0.5s ease-out';
        return element;
    }

    createRecommendationCard(recommendation) {
        const card = document.createElement('div');
        card.className = `recommendation-card`;
        card.setAttribute('data-priority', recommendation.priority);
        card.innerHTML = `
            <div class="rec-icon">
                <i class="${recommendation.icon}"></i>
            </div>
            <div class="rec-content">
                <h5>${recommendation.title}</h5>
                <p>${recommendation.content}</p>
                <div class="rec-metrics">
                    <span class="metric-tag">${recommendation.metric}</span>
                </div>
            </div>
            <button class="rec-action">${recommendation.action}</button>
        `;
        card.style.animation = 'fadeInUp 0.6s ease-out';
        return card;
    }

    createActivityElement(activity) {
        const element = document.createElement('div');
        element.className = `review-item ${activity.type}`;
        element.innerHTML = `
            <div class="review-avatar">${activity.avatar}</div>
            <div class="review-content">
                <div class="review-text">${activity.text}</div>
                <div class="review-meta">${activity.meta}</div>
            </div>
        `;
        return element;
    }

    // Animation helper
    animateValue(element, targetValue) {
        const current = parseFloat(element.textContent.replace(/[^0-9.-]+/g, "")) || 0;
        const duration = 1000;
        const start = performance.now();
        const startValue = current;

        const animate = (currentTime) => {
            const elapsed = currentTime - start;
            const progress = Math.min(elapsed / duration, 1);
            
            const easeOut = 1 - Math.pow(1 - progress, 4);
            const value = startValue + (targetValue - startValue) * easeOut;

            if (element.id.includes('Rate') || element.id.includes('Score')) {
                element.textContent = value.toFixed(1) + '%';
            } else if (element.id.includes('Revenue')) {
                element.textContent = '$' + Math.floor(value).toLocaleString();
            } else {
                element.textContent = Math.floor(value).toLocaleString();
            }

            if (progress < 1) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }

    // Mock data generators
    generateMockOrders() {
        const locations = ['New York', 'London', 'Tokyo', 'Paris', 'Sydney'];
        const count = Math.floor(Math.random() * 2) + 1; // 1-2 new orders

        return Array.from({ length: count }, () => ({
            id: `#ORD-${Math.floor(Math.random() * 9000) + 1000}`,
            location: locations[Math.floor(Math.random() * locations.length)],
            amount: `$${(Math.random() * 80 + 20).toFixed(2)}`,
            time: 'Just now'
        }));
    }

    generateAIRecommendations() {
        const templates = [
            {
                priority: 'high',
                icon: 'fas fa-fire',
                title: 'TRENDING NOW',
                content: 'Spicy Dragon Roll trending +42% this hour',
                metric: '+$1,240 potential',
                action: 'OPTIMIZE'
            },
            {
                priority: 'medium',
                icon: 'fas fa-chart-line', 
                title: 'PRICE OPTIMIZATION',
                content: 'Increase Truffle Fries by 15% for max profit',
                metric: '87% confidence',
                action: 'APPLY'
            },
            {
                priority: 'critical',
                icon: 'fas fa-exclamation-triangle',
                title: 'STOCK ALERT', 
                content: 'Avocado stock critical - 3.2 hours remaining',
                metric: 'URGENT',
                action: 'ORDER NOW'
            }
        ];

        // Randomly select 2-3 recommendations
        const count = Math.floor(Math.random() * 2) + 2;
        const shuffled = [...templates].sort(() => 0.5 - Math.random());
        return shuffled.slice(0, count);
    }

    generateCustomerActivity() {
        const activities = [
            {
                type: 'positive',
                avatar: '😊',
                text: '"The AI recommendations are absolutely mind-blowing! 🤯"',
                meta: '- TechFoodie • 2 mins ago'
            },
            {
                type: 'positive', 
                avatar: '⚡',
                text: '"Lightning fast delivery and the food was incredible!"',
                meta: '- Sarah M • 5 mins ago'
            },
            {
                type: 'neutral',
                avatar: '🤔',
                text: '"Good experience, but the app interface could be smoother"',
                meta: '- Mike R • 8 mins ago'
            }
        ];

        const count = Math.floor(Math.random() * 2) + 1;
        const shuffled = [...activities].sort(() => 0.5 - Math.random());
        return shuffled.slice(0, count);
    }

    // Event system
    on(event, callback) {
        if (!this.updateCallbacks.has(event)) {
            this.updateCallbacks.set(event, []);
        }
        this.updateCallbacks.get(event).push(callback);
    }

    emit(event, data) {
        const callbacks = this.updateCallbacks.get(event) || [];
        callbacks.forEach(callback => callback(data));
    }

    // Performance monitoring
    setupPerformanceMonitoring() {
        // Monitor frame rate
        this.monitorFrameRate();
        
        // Monitor memory usage
        this.monitorMemoryUsage();
        
        // Monitor connection health
        this.monitorConnectionHealth();
    }

    monitorFrameRate() {
        let frameCount = 0;
        let lastTime = performance.now();
        
        const checkFrameRate = () => {
            frameCount++;
            const currentTime = performance.now();
            
            if (currentTime - lastTime >= 1000) {
                const fps = Math.round((frameCount * 1000) / (currentTime - lastTime));
                
                if (fps < 30) {
                    console.warn(`Low frame rate: ${fps} FPS`);
                }
                
                frameCount = 0;
                lastTime = currentTime;
            }
            
            requestAnimationFrame(checkFrameRate);
        };
        
        checkFrameRate();
    }

    monitorMemoryUsage() {
        if (performance.memory) {
            setInterval(() => {
                const used = performance.memory.usedJSHeapSize;
                const limit = performance.memory.jsHeapSizeLimit;
                
                if (used / limit > 0.8) {
                    console.warn('High memory usage detected');
                }
            }, 10000);
        }
    }

    monitorConnectionHealth() {
        setInterval(() => {
            // Simulate connection health check
            const health = Math.random() > 0.1 ? 'healthy' : 'degraded';
            
            if (health === 'degraded') {
                console.warn('Connection health degraded');
                this.emit('connection:degraded');
            }
        }, 30000);
    }

    // Cleanup
    destroy() {
        this.dataStreams.forEach(stream => {
            if (stream.intervalId) {
                clearInterval(stream.intervalId);
            }
        });
        
        this.connections.clear();
        this.dataStreams.clear();
        this.updateCallbacks.clear();
    }
}

// Initialize real-time engine
window.realTimeEngine = new RealTimeEngine();

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = RealTimeEngine;
}