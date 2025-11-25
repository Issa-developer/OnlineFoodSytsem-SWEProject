const CONFIG = {
    DELIVERY_FEE: 150,
    TAX_RATE: 0.16,
    CURRENCY: 'KES',
    CURRENCY_SYMBOL: 'KES',
    PROMO_CODES: {
        'SAVE10': { discount: 0.10, type: 'percentage', description: '10% off entire order' },
        'SAVE20': { discount: 0.20, type: 'percentage', description: '20% off entire order' },
        'FLAT50': { discount: 50, type: 'fixed', description: 'KES 50 off' },
        'WELCOME15': { discount: 0.15, type: 'percentage', description: '15% off for new users' },
        'FREEDELIV': { discount: 'FREE_DELIVERY', type: 'special', description: 'Free delivery' }
    },
    MAX_QUANTITY: 10,
    MIN_QUANTITY: 1
};

// GLOBAL STATE

let cartData = {
    items: [],
    subtotal: 0,
    deliveryFee: CONFIG.DELIVERY_FEE,
    tax: 0,
    discount: 0,
    discountType: null,
    total: 0,
    promoCode: null
};

// ========================================
// INITIALIZATION
// ========================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('Cart page initialized');
    initializeCart();
    loadCartFromStorage();
    updateCartDisplay();
    attachEventListeners();
    updateNavCartCount();
});

function initializeCart() {
    if (cartData.items.length === 0 && document.querySelectorAll('.cart-item').length > 0) {
        loadCartFromDOM();
    }
}

function loadCartFromDOM() {
    const cartItems = document.querySelectorAll('.cart-item');
    cartItems.forEach(item => {
        const itemId = item.getAttribute('data-item-id');
        const itemName = item.querySelector('.item-name').textContent;
        const itemPrice = parseFloat(item.querySelector('.item-price').textContent.replace(/[^0-9.]/g, ''));
        const quantity = parseInt(item.querySelector('.qty-input').value);

        cartData.items.push({
            id: itemId,
            name: itemName,
            price: itemPrice,
            quantity: quantity,
            subtotal: itemPrice * quantity
        });
    });
}

function loadCartFromStorage() {
    const stored = localStorage.getItem('cart');
    if (stored) {
        try {
            cartData = JSON.parse(stored);
        } catch (e) {
            console.error('Error loading cart from storage:', e);
        }
    }
}

function saveCartToStorage() {
    localStorage.setItem('cart', JSON.stringify(cartData));
}

function attachEventListeners() {
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('qty-decrease')) {
            const itemId = e.target.closest('.cart-item').getAttribute('data-item-id');
            decreaseQuantity(itemId);
        }
        if (e.target.classList.contains('qty-increase')) {
            const itemId = e.target.closest('.cart-item').getAttribute('data-item-id');
            increaseQuantity(itemId);
        }
        if (e.target.classList.contains('btn-remove')) {
            const itemId = e.target.closest('.cart-item').getAttribute('data-item-id');
            removeItem(itemId);
        }
    });

    const promoInput = document.getElementById('promo-code');
    if (promoInput) {
        promoInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                applyPromoCode();
            }
        });
    }
}

// ========================================
// QUANTITY MANAGEMENT
// ========================================

function increaseQuantity(itemId) {
    const item = document.querySelector(`[data-item-id="${itemId}"]`);
    const quantityInput = item.querySelector('.qty-input');
    let currentQuantity = parseInt(quantityInput.value);

    if (currentQuantity < CONFIG.MAX_QUANTITY) {
        currentQuantity++;
        quantityInput.value = currentQuantity;
        updateQuantity(itemId, currentQuantity);
    } else {
        showToast(`Maximum quantity (${CONFIG.MAX_QUANTITY}) reached!`, 'warning');
    }
}

function decreaseQuantity(itemId) {
    const item = document.querySelector(`[data-item-id="${itemId}"]`);
    const quantityInput = item.querySelector('.qty-input');
    let currentQuantity = parseInt(quantityInput.value);

    if (currentQuantity > CONFIG.MIN_QUANTITY) {
        currentQuantity--;
        quantityInput.value = currentQuantity;
        updateQuantity(itemId, currentQuantity);
    } else {
        showToast('Minimum quantity is 1. Remove item instead?', 'warning');
    }
}

function updateQuantity(itemId, quantity) {
    quantity = parseInt(quantity);

    if (quantity < CONFIG.MIN_QUANTITY || quantity > CONFIG.MAX_QUANTITY) {
        showToast(`Quantity must be between ${CONFIG.MIN_QUANTITY} and ${CONFIG.MAX_QUANTITY}`, 'error');
        return;
    }

    const item = document.querySelector(`[data-item-id="${itemId}"]`);
    if (!item) return;

    const price = parseFloat(item.querySelector('.item-price').textContent.replace(/[^0-9.]/g, ''));
    const subtotal = price * quantity;

    item.querySelector('.subtotal-amount').textContent = formatCurrency(subtotal);

    const cartItem = cartData.items.find(i => i.id === itemId.toString());
    if (cartItem) {
        cartItem.quantity = quantity;
        cartItem.subtotal = subtotal;
    }

    calculateTotals();
    updateCartDisplay();
    saveCartToStorage();

    showToast(`Quantity updated to ${quantity}`, 'success');
}

// ========================================
// CART ITEM MANAGEMENT
// ========================================

function removeItem(itemId) {
    const item = document.querySelector(`[data-item-id="${itemId}"]`);
    if (!item) return;

    const itemName = item.querySelector('.item-name').textContent;

    item.style.animation = 'fadeOut 0.3s ease-out forwards';
    
    setTimeout(() => {
        item.remove();

        cartData.items = cartData.items.filter(i => i.id !== itemId.toString());

        calculateTotals();
        updateCartDisplay();
        saveCartToStorage();

        showToast(`${itemName} removed from cart`, 'success');

        if (cartData.items.length === 0) {
            showEmptyCart();
        }
    }, 300);
}

function clearCart() {
    if (cartData.items.length === 0) {
        showToast('Cart is already empty', 'warning');
        return;
    }

    const confirmed = confirm('Are you sure you want to clear the entire cart? This action cannot be undone.');
    if (!confirmed) return;

    const items = document.querySelectorAll('.cart-item');
    items.forEach(item => {
        item.style.animation = 'fadeOut 0.3s ease-out forwards';
    });

    setTimeout(() => {
        items.forEach(item => item.remove());
        cartData.items = [];
        cartData.promoCode = null;
        cartData.discount = 0;
        cartData.discountType = null;
        
        calculateTotals();
        updateCartDisplay();
        saveCartToStorage();
        showEmptyCart();
        showToast('Cart cleared successfully', 'success');
    }, 300);
}

function addToCart(itemName, price) {
    // Check if item already exists in cart
    let existing = cartData.items.find(i => i.name === itemName);
    if (existing) {
        if (existing.quantity < CONFIG.MAX_QUANTITY) {
            existing.quantity++;
            existing.subtotal = existing.price * existing.quantity;
            showToast(`${itemName} quantity increased!`, 'success');
        } else {
            showToast(`Maximum quantity (${CONFIG.MAX_QUANTITY}) reached!`, 'warning');
            return;
        }
    } else {
        const newItem = {
            id: Date.now().toString(),
            name: itemName,
            price: price,
            quantity: 1,
            subtotal: price
        };
        cartData.items.push(newItem);
        showToast(`${itemName} added to cart!`, 'success');
    }
    calculateTotals();
    updateCartDisplay();
    saveCartToStorage();
    updateNavCartCount();
}

// Update cart count in navigation bar
function updateNavCartCount() {
    let navCart = document.querySelector('.nav-menu .cart-count');
    if (!navCart) {
        // Add cart count span if not present
        let cartLink = document.querySelector('.nav-menu a[href="cart.html"]');
        if (cartLink) {
            navCart = document.createElement('span');
            navCart.className = 'cart-count';
            cartLink.appendChild(navCart);
        }
    }
    if (navCart) {
        const totalItems = cartData.items.reduce((sum, item) => sum + item.quantity, 0);
        navCart.textContent = totalItems > 0 ? ` (${totalItems})` : '';
    }
}

// ========================================
// PRICE CALCULATIONS
// ========================================

function calculateTotals() {
    cartData.subtotal = cartData.items.reduce((sum, item) => sum + item.subtotal, 0);

    if (cartData.promoCode) {
        const promo = CONFIG.PROMO_CODES[cartData.promoCode];
        if (promo) {
            if (promo.type === 'percentage') {
                cartData.discount = cartData.subtotal * promo.discount;
            } else if (promo.type === 'fixed') {
                cartData.discount = promo.discount;
            } else if (promo.type === 'special' && promo.discount === 'FREE_DELIVERY') {
                cartData.discount = 0;
            }
        }
    }

    let taxableAmount = cartData.subtotal - cartData.discount;
    if (cartData.discountType === 'FREE_DELIVERY') {
        taxableAmount += CONFIG.DELIVERY_FEE;
    }

    cartData.tax = Math.round(taxableAmount * CONFIG.TAX_RATE * 100) / 100;

    let total = cartData.subtotal + cartData.tax + cartData.deliveryFee - cartData.discount;

    if (cartData.discountType === 'FREE_DELIVERY') {
        total -= cartData.deliveryFee;
        cartData.deliveryFee = 0;
    } else {
        cartData.deliveryFee = CONFIG.DELIVERY_FEE;
    }

    cartData.total = Math.round(total * 100) / 100;
}

function formatCurrency(amount) {
    return `${CONFIG.CURRENCY_SYMBOL} ${amount.toLocaleString('en-KE', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })}`;
}

// ========================================
// PROMO CODE HANDLING
// ========================================

function applyPromoCode() {
    const promoInput = document.getElementById('promo-code');
    const promoMessage = document.getElementById('promo-message');
    const code = promoInput.value.trim().toUpperCase();

    promoMessage.textContent = '';
    promoMessage.className = 'promo-message';

    if (!code) {
        promoMessage.textContent = 'Please enter a promo code';
        promoMessage.classList.add('error');
        return;
    }

    if (!CONFIG.PROMO_CODES[code]) {
        promoMessage.textContent = '❌ Invalid promo code';
        promoMessage.classList.add('error');
        showToast('Invalid promo code', 'error');
        return;
    }

    const promo = CONFIG.PROMO_CODES[code];
    cartData.promoCode = code;
    cartData.discountType = promo.type;

    calculateTotals();
    updateCartDisplay();
    saveCartToStorage();

    promoMessage.textContent = `✓ ${promo.description} applied!`;
    promoMessage.classList.add('success');

    showToast(`Promo code "${code}" applied successfully!`, 'success');

    promoInput.value = '';
}

function removePromoCode() {
    cartData.promoCode = null;
    cartData.discountType = null;
    cartData.discount = 0;

    document.getElementById('promo-code').value = '';
    document.getElementById('promo-message').textContent = '';
    document.getElementById('promo-message').className = 'promo-message';

    calculateTotals();
    updateCartDisplay();
    saveCartToStorage();

    showToast('Promo code removed', 'success');
}

// ========================================
// DISPLAY UPDATES
// ========================================

function updateCartDisplay() {
    updateCartItemCount();
    updateSummary();
    updateDiscountDisplay();
    toggleEmptyCartDisplay();
}

function updateCartItemCount() {
    const countElement = document.getElementById('cart-item-count');
    const totalItems = cartData.items.reduce((sum, item) => sum + item.quantity, 0);
    countElement.textContent = `${totalItems} item${totalItems !== 1 ? 's' : ''} in cart`;
}

function updateSummary() {
    document.getElementById('subtotal').textContent = formatCurrency(cartData.subtotal);

    const deliveryElement = document.getElementById('delivery-fee');
    if (cartData.discountType === 'FREE_DELIVERY') {
        deliveryElement.textContent = 'FREE ✓';
        deliveryElement.style.color = '#27AE60';
    } else {
        deliveryElement.textContent = formatCurrency(cartData.deliveryFee);
        deliveryElement.style.color = '';
    }

    document.getElementById('tax').textContent = formatCurrency(cartData.tax);
    document.getElementById('total').textContent = formatCurrency(cartData.total);
}

function updateDiscountDisplay() {
    const discountDisplay = document.getElementById('discount-display');
    const discountAmount = document.getElementById('discount-amount');

    if (cartData.discount > 0) {
        discountAmount.textContent = `- ${formatCurrency(cartData.discount)}`;
        discountDisplay.style.display = 'flex';
    } else {
        discountDisplay.style.display = 'none';
    }
}

function toggleEmptyCartDisplay() {
    const emptyMessage = document.getElementById('empty-cart-message');
    const container = document.getElementById('cart-items-container');

    if (cartData.items.length === 0) {
        if (container) container.style.display = 'none';
        if (emptyMessage) emptyMessage.style.display = 'flex';
    } else {
        if (container) container.style.display = 'flex';
        if (emptyMessage) emptyMessage.style.display = 'none';
    }
}

// ========================================
// CHECKOUT FUNCTIONS
// ========================================

function proceedToCheckout() {
    if (cartData.items.length === 0) {
        showToast('Cart is empty', 'error');
        return;
    }

    const checkoutData = {
        items: cartData.items,
        subtotal: cartData.subtotal,
        tax: cartData.tax,
        deliveryFee: cartData.deliveryFee,
        discount: cartData.discount,
        promoCode: cartData.promoCode,
        total: cartData.total,
        timestamp: new Date().toISOString()
    };

    localStorage.setItem('checkout_data', JSON.stringify(checkoutData));

    window.location.href = 'checkout.html';
}

function saveForLater() {
    if (cartData.items.length === 0) {
        showToast('Cart is empty', 'warning');
        return;
    }

    const savedCart = {
        items: cartData.items,
        savedAt: new Date().toISOString()
    };

    localStorage.setItem('saved_cart', JSON.stringify(savedCart));
    showToast('Cart saved! You can restore it anytime.', 'success');
}

// ========================================
// TOAST NOTIFICATIONS
// ========================================

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// ========================================
// UTILITY FUNCTIONS
// ========================================

function getCartSummary() {
    return {
        itemCount: cartData.items.length,
        totalItems: cartData.items.reduce((sum, item) => sum + item.quantity, 0),
        subtotal: cartData.subtotal,
        tax: cartData.tax,
        deliveryFee: cartData.deliveryFee,
        discount: cartData.discount,
        total: cartData.total
    };
}

function exportCartData() {
    console.log('Current Cart Data:', JSON.stringify(getCartSummary(), null, 2));
    return getCartSummary();
}

// ========================================
// ANIMATIONS
// ========================================

const style = document.createElement('style');
style.textContent = `
    @keyframes fadeOut {
        from {
            opacity: 1;
            transform: translateX(0);
        }
        to {
            opacity: 0;
            transform: translateX(20px);
        }
    }
`;
document.head.appendChild(style);

console.log('✓ Cart.js loaded successfully');