<template>
  <div
    style="min-height:80vh;display:flex;align-items:center;justify-content:center;padding:20px;
    background:linear-gradient(135deg,#f2d4ba 0%,#e6bca1 100%);
    font-family:'Segoe UI',sans-serif;color:#3a2d1b;"
  >
    <div
      style="background:#fffaf5;border-radius:20px;padding:40px;max-width:500px;width:100%;
      text-align:center;box-shadow:0 8px 24px rgba(0,0,0,0.15);
      transform:translateY(0);transition:transform 0.3s ease;"
    >
      <div style="margin-bottom:24px;">
        <svg
          viewBox='0 0 24 24'
          style="width:80px;height:80px;color:#d95a4a;animation:scaleIn 0.6s ease-out;"
        >
          <path
            fill='currentColor'
            d='M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M12 20C7.59 20 4 16.41 4 12S7.59 4 12 4 20 7.59 20 12 16.41 20 12 20M15.59 7L12 10.59L8.41 7L7 8.41L10.59 12L7 15.59L8.41 17L12 13.41L15.59 17L17 15.59L13.41 12L17 8.41L15.59 7Z'
          />
        </svg>
      </div>

      <h1 style="font-size:26px;margin-bottom:12px;color:#5e3c1b;">
        💔 Pago Rechazado
      </h1>
      <p style="font-size:16px;color:#7a6a56;margin-bottom:20px;">
        Lo sentimos, tu pago no pudo ser procesado.
      </p>

      <div
        v-if="paymentInfo"
        style="background:#fff3ef;border-radius:10px;padding:16px;margin-bottom:24px;
        text-align:left;box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'rejected' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div
        style="background:#faf3e0;border-radius:10px;padding:18px;text-align:left;margin-bottom:24px;
        box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <h3 style="font-size:16px;color:#4d3319;margin-bottom:8px;">Posibles causas:</h3>
        <ul style="list-style:none;padding:0;margin:0;color:#7a6a56;">
          <li style="padding:4px 0;">🍂 Fondos insuficientes</li>
          <li style="padding:4px 0;">🍂 Datos de tarjeta incorrectos</li>
          <li style="padding:4px 0;">🍂 La tarjeta está vencida</li>
          <li style="padding:4px 0;">🍂 El pago fue rechazado por el banco</li>
        </ul>
      </div>

      <div style="display:flex;flex-direction:column;gap:12px;">
        <button
          @click="intentarNuevamente"
          style="padding:12px;border:none;border-radius:10px;background:#e1785c;
          color:white;font-size:16px;font-weight:600;cursor:pointer;
          box-shadow:0 4px 10px rgba(225,120,92,0.4);transition:all 0.3s;"
          @mouseover="hoverBtn='retry'"
          @mouseleave="hoverBtn=null"
          :style="{background:hoverBtn==='retry'?'#cf654b':'#e1785c',transform:hoverBtn==='retry'?'scale(1.03)':'scale(1)'}"
        >
          🔁 Intentar Nuevamente
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
  console.log('Pago rechazado - Info:', paymentInfo.value)
})

const intentarNuevamente = () => router.push('/carrito')
const irAHome = () => router.push('/')
</script>
