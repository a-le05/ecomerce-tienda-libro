<template>
  <div
    style="min-height:80vh;display:flex;align-items:center;justify-content:center;padding:20px;
    background:linear-gradient(135deg,#b7f7d0 0%,#8cf3ac 100%);
    font-family:'Segoe UI',sans-serif;color:#1d3a2b;"
  >
    <div
      style="background:#f0fff8;border-radius:20px;padding:40px;max-width:500px;width:100%;
      text-align:center;box-shadow:0 8px 24px rgba(0,0,0,0.15);"
    >
      <div style="margin-bottom:24px;position:relative;display:inline-block;">
        <svg
          viewBox='0 0 24 24'
          style="width:80px;height:80px;color:#1fa765;animation:scaleIn 0.6s ease-out;"
        >
          <path
            fill='currentColor'
            d='M12 2C6.5 2 2 6.5 2 12S6.5 22 12 22 22 17.5 22 12 17.5 2 12 2M10 17L5 12L6.41 10.59L10 14.17L17.59 6.58L19 8L10 17Z'
          />
        </svg>

        <!-- Efecto de halo brillante -->
        <div
          style="position:absolute;top:50%;left:50%;width:100px;height:100px;
          margin:-50px 0 0 -50px;border:4px solid #d2fbe0;border-top:4px solid #1fa765;
          border-radius:50%;animation:spin 2s linear infinite;"
        ></div>
      </div>

      <h1 style="font-size:26px;margin-bottom:12px;color:#1b5e20;">
        ✅ Pago Exitoso
      </h1>
      <p style="font-size:16px;color:#375e47;margin-bottom:20px;">
        Tu pago ha sido procesado correctamente 🎉  
      </p>

      <div
        v-if="paymentInfo"
        style="background:#e6f9ed;border-radius:10px;padding:16px;margin-bottom:24px;
        text-align:left;box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <p v-if="paymentInfo.payment_id"><strong>ID de Pago:</strong> {{ paymentInfo.payment_id }}</p>
        <p><strong>Estado:</strong> {{ paymentInfo.status || 'approved' }}</p>
        <p v-if="paymentInfo.external_reference">
          <strong>Número de Pedido:</strong> #{{ paymentInfo.external_reference }}
        </p>
      </div>

      <div
        style="background:#ebfff2;border-left:4px solid #2ecc71;border-radius:10px;
        padding:20px;margin-bottom:24px;display:flex;gap:12px;text-align:left;"
      >
        <svg viewBox='0 0 24 24' style="width:28px;height:28px;color:#1fa765;flex-shrink:0;">
          <path fill='currentColor' d='M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2m-1 15-5-5 1.41-1.41L11 13.17l6.59-6.58L19 8l-8 9Z'/>
        </svg>
        <div>
          <h3 style="font-size:16px;color:#1b4d35;margin-bottom:6px;">¡Pago completado con éxito!</h3>
          <p style="font-size:14px;color:#2e6044;margin:4px 0;">
            Tu transacción fue aprobada y registrada correctamente.
          </p>
          <p style="font-size:12px;color:#3a7350;font-style:italic;margin-top:6px;">
            Recibirás la confirmación y el comprobante por correo electrónico.
          </p>
        </div>
      </div>

      <div
        style="background:#f2fff6;border-radius:10px;padding:18px;text-align:left;margin-bottom:24px;
        box-shadow:inset 0 0 6px rgba(0,0,0,0.05);"
      >
        <h3 style="font-size:16px;color:#1b4d35;margin-bottom:8px;">
          🌿 ¿Qué puedes hacer ahora?
        </h3>
        <ul style="list-style:none;padding:0;margin:0;color:#2e6044;">
          <li style="padding:4px 0;">🍀 Ver el detalle de tus compras recientes</li>
          <li style="padding:4px 0;">🍀 Descargar tu comprobante si lo deseas</li>
          <li style="padding:4px 0;">🍀 Continuar explorando nuevos productos</li>
        </ul>
      </div>

      <div style="display:flex;flex-direction:column;gap:12px;">
        <button
          @click="irAMisCompras"
          style="padding:12px;border:none;border-radius:10px;background:#1fa765;
          color:white;font-size:16px;font-weight:600;cursor:pointer;
          box-shadow:0 4px 10px rgba(31,167,101,0.4);transition:all 0.3s;"
          @mouseover="hoverBtn='compras'"
          @mouseleave="hoverBtn=null"
          :style="{background:hoverBtn==='compras'?'#179458':'#1fa765',transform:hoverBtn==='compras'?'scale(1.03)':'scale(1)'}"
        >
          🌿 Ver Mis Compras
        </button>

        <button
          @click="irAHome"
          style="padding:12px;border:2px solid #a8d8b6;border-radius:10px;background:white;
          color:#1b4d35;font-size:16px;font-weight:600;cursor:pointer;transition:all 0.3s;"
          @mouseover="hoverBtn='home'"
          @mouseleave="hoverBtn=null"
          :style="{background:hoverBtn==='home'?'#eafff1':'white',transform:hoverBtn==='home'?'scale(1.03)':'scale(1)'}"
        >
          🏡 Volver al Inicio
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
  console.log('Pago exitoso - Info:', paymentInfo.value)
})

const irAMisCompras = () => router.push('/mis-compras')
const irAHome = () => router.push('/')
</script>

<style>
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
@keyframes scaleIn {
  from { transform: scale(0); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}
</style>


