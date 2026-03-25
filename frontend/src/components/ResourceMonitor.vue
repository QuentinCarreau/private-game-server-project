<template>
  <div class="resource-monitor">
    <div class="monitor-item">
      <div class="label">CPU</div>
      <div class="progress-bar-bg">
        <div class="progress-fill cpu" :style="{ width: cpuUsage + '%' }"></div>
      </div>
      <div class="percentage">{{ Math.round(cpuUsage) }}%</div>
    </div>
    
    <div class="monitor-item">
      <div class="label">RAM</div>
      <div class="progress-bar-bg">
        <div class="progress-fill ram" :style="{ width: ramUsage + '%' }"></div>
      </div>
      <div class="percentage">{{ Math.round(ramUsage) }}%</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const cpuUsage = ref(35);
const ramUsage = ref(58);
let interval = null;

onMounted(() => {
  interval = setInterval(() => {
    cpuUsage.value = Math.max(5, Math.min(95, cpuUsage.value + (Math.random() * 8 - 4)));
    ramUsage.value = Math.max(10, Math.min(90, ramUsage.value + (Math.random() * 2 - 1)));
  }, 3000);
});

onUnmounted(() => clearInterval(interval));
</script>

<style scoped>
.resource-monitor {
  display: flex;
  gap: 3rem;
  background: var(--bg-card);
  padding: 1.25rem 2rem;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
}

.monitor-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 180px;
}

.label {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.progress-bar-bg {
  flex-grow: 1;
  height: 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 1s ease;
}

.progress-fill.cpu { background: var(--accent-primary); }
.progress-fill.ram { background: var(--accent-secondary); }

.percentage {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-primary);
  min-width: 35px;
  text-align: right;
}

@media (max-width: 768px) {
  .resource-monitor {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
}
</style>
