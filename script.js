// Shared validation + interactivity for both pages


// Helpers
function qs(sel){return document.querySelector(sel)}
function qsa(sel){return Array.from(document.querySelectorAll(sel))}


// Password toggle (works for any page)
document.addEventListener('click', e=>{
if(e.target.matches('.toggle-pw')){
const wrap = e.target.closest('.field') || e.target.closest('.password-wrap')
const input = wrap.querySelector('input')
if(input.type === 'password'){ input.type = 'text'; e.target.textContent = '🙈' }
else { input.type = 'password'; e.target.textContent = '👁️' }
}
})


// Basic validators
function isEmail(v){ return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) }
function isPhone(v){ return /^\+?[0-9 \-]{7,15}$/.test(v) }


function showError(input, msg){
const el = document.querySelector(`.error[data-for="${input.id}"]`)
if(el) el.textContent = msg || ''
}


function setBtnState(formId, btnId, checkFn){
const form = document.getElementById(formId)
const btn = document.getElementById(btnId)
if(!form || !btn) return
const toggle = ()=> btn.disabled = !checkFn()
form.addEventListener('input', toggle)
toggle()
}


// Login: validate email & password not empty
if(document.getElementById('loginForm')){
const form = document.getElementById('loginForm')
const email = document.getElementById('loginEmail')
const pw = document.getElementById('loginPassword')
setBtnState('loginForm','loginBtn', ()=> isEmail(email.value.trim()) && pw.value.trim().length>0)


form.addEventListener('submit', e=>{
e.preventDefault()
let ok = true
if(!isEmail(email.value.trim())){ showError(email,'Enter a valid email'); ok=false } else showError(email,'')
if(pw.value.trim().length===0){ showError(pw,'Password required'); ok=false } else showError(pw,'')
if(!ok) return
// Mock success — connect to backend here
alert('Login successful (mock) — implement API call')
form.reset()
})
}


// Signup: many checks
if(document.getElementById('signupForm')){
const form = document.getElementById('signupForm')
const fullname = document.getElementById('fullName')
const email = document.getElementById('signupEmail')
const phone = document.getElementById('phone')
const pw = document.getElementById('signupPassword')
const confirm = document.getElementById('confirmPassword')
}