<template>
  <div
    style="min-height:80vh;display:flex;align-items:center;justify-content:center;padding:20px;
    background:linear-gradient(135deg,#f7e2b7 0%,#f3ca8c 100%);
    font-family:'Segoe UI',sans-serif;color:#3a2d1b;"
  >
    <div
      style="background:#fffaf0;border-radius:20px;padding:40px;max-width:500px;width:100%;
      text-align:center;box-shadow:0 8px 24px rgba(0,0,0,0.15);"
    >
      <div style="margin-bottom:24px;position:relative;display:inline-block;">
        <svg
          viewBox='0 0 24 24'
          style="width:80px;height:80px;color:#e4a03d;animation:scaleIn 0.6s ease-out;"
        >
          <path
            fill='currentColor'
            d='M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M12 20C7.59 20 4 16.41 4 12S7.59 4 12 4 20 7.59 20 12 16.41 20 12 20M16.59 7.58L10 14.17L7.41 11.59L6 13L10 17L18 9L16.59 7.58Z'
          />
        </svg>

        <!-- Spinner -->
        <div
          style="position:absolute;top:50%;left:50%;width:100px;height:100px;
          margin:-50px 0 0 -50px;border:4px solid #f8ecd6;border-top:4px solid #e4a03d;
          border-radius:50%;animation:spin 1.6s linear infinite;"
        ></div>
      </div>

      <h1 style="font-size:26px;margin-bottom:12px;color:#5e3c1b;">
        ⏳ Pago Pendiente
      </h1>
      <p style="font-size:16px;color:#7a6a56;margin-bottom:20px;">
        Tu pago está siendo procesado.  
      </p>

      <div
        v-if="paymentInfo"
        style="background:#fff6e1;border-radius:10px;padding:16px;margin-bottom:24px;
        text-align:left;box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'pending' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div
        style="background:#f8edda;border-left:4px solid #e4a03d;border-radius:10px;
        padding:20px;margin-bottom:24px;display:flex;gap:12px;text-align:left;"
      >
        <svg viewBox='0 0 24 24' style="width:28px;height:28px;color:#d88b32;flex-shrink:0;">
          <path fill='currentColor' d='M13 14H11V9H13M13 18H11V16H13M1 21H23L12 2L1 21Z'/>
        </svg>
        <div>
          <h3 style="font-size:16px;color:#4d3319;margin-bottom:6px;">¿Qué significa esto?</h3>
          <p style="font-size:14px;color:#6b5b46;margin:4px 0;">
            Tu pago está siendo verificado. Te notificaremos por email cuando se complete.
          </p>
          <p style="font-size:12px;color:#857660;font-style:italic;margin-top:6px;">
            Esto puede tardar entre unos minutos y 48 horas hábiles.
          </p>
        </div>
      </div>

      <div
        style="background:#faf3e0;border-radius:10px;padding:18px;text-align:left;margin-bottom:24px;
        box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <h3 style="font-size:16px;color:#4d3319;margin-bottom:8px;">
          🌿 Motivos comunes de pago pendiente:
        </h3>
        <ul style="list-style:none;padding:0;margin:0;color:#7a6a56;">
          <li style="padding:4px 0;">🍂 Pago en efectivo (Rapipago, Pago Fácil, etc.)</li>
          <li style="padding:4px 0;">🍂 Transferencia bancaria en proceso</li>
          <li style="padding:4px 0;">🍂 Verificación adicional del banco</li>
          <li style="padding:4px 0;">🍂 Pago con saldo de Mercado Pago</li>
        </ul>
      </div>

      <div style="display:flex;flex-direction:column;gap:12px;">
        <button
          @click="irAMisCompras"
          style="padding:12px;border:none;border-radius:10px;background:#e4a03d;
          color:white;font-size:16px;font-weight:600;cursor:pointer;
          box-shadow:0 4px 10px rgba(228,160,61,0.4);transition:all 0.3s;"
          @mouseover="hoverBtn='compras'"
          @mouseleave="hoverBtn=null"
          :style="{background:hoverBtn==='compras'?'#d68d28':'#e4a03d',transform:hoverBtn==='compras'?'scale(1.03)':'scale(1)'}"
        >
          📚 Ver Mis Compras
        </button>

        <button
          @click="irAHome"
          style="padding:12px;border:2px solid #d6c6a8;border-radius:10px;background:white;
          color:#5c4a2e;font-size:16px;font-weight:600;cursor:pointer;transition:all 0.3s;"
          @mouseover="hoverBtn='home'"
          @mouseleave="hoverBtn=null"
          :style="{background:hoverBtn==='home'?'#fff6e6':'white',transform:hoverBtn==='home'?'scale(1.03)':'scale(1)'}"
        >
          🏠 Volver al Inicio
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const paymentInfo = ref(null)
const hoverBtn = ref(null)

onMounted(() => {
  paymentInfo.value = {
    payment_id: route.query.payment_id,
    status: route.query.status,
    external_reference: route.query.external_reference,
    merchant_order_id: route.query.merchant_order_id
  }
  console.log('Pago pendiente - Info:', paymentInfo.value)
})

const irAMisCompras = () => router.push('/mis-compras')
const irAHome = () => router.push('/')
</script>

