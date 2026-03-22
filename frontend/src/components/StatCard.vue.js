import { computed } from 'vue';
const props = defineProps();
const iconMap = {
    bell: '铃',
    money: '费',
    warning: '警',
    document: '单',
    service: '诉',
    clock: '时',
    tool: '修',
    repair: '办',
};
const iconText = computed(() => iconMap[props.stat.icon] ?? '数');
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['stat-card__label']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
    ...{ class: "stat-card glass-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "stat-card__icon" },
    ...{ style: ({ background: __VLS_ctx.stat.accent, color: __VLS_ctx.stat.color }) },
});
(__VLS_ctx.iconText);
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "stat-card__value" },
});
(__VLS_ctx.stat.value);
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "stat-card__label" },
});
(__VLS_ctx.stat.label);
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
(__VLS_ctx.stat.unit);
/** @type {__VLS_StyleScopedClasses['stat-card']} */ ;
/** @type {__VLS_StyleScopedClasses['glass-card']} */ ;
/** @type {__VLS_StyleScopedClasses['stat-card__icon']} */ ;
/** @type {__VLS_StyleScopedClasses['stat-card__value']} */ ;
/** @type {__VLS_StyleScopedClasses['stat-card__label']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            iconText: iconText,
        };
    },
    __typeProps: {},
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
    __typeProps: {},
});
; /* PartiallyEnd: #4569/main.vue */
