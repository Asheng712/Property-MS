import { InfoFilled } from '@element-plus/icons-vue';
import { computed } from 'vue';
const props = defineProps();
const pieBackground = computed(() => {
    let current = 0;
    const entries = props.segments.map((segment) => {
        const start = current;
        current += segment.value;
        return `${segment.color} ${start}% ${current}%`;
    });
    return `conic-gradient(${entries.join(', ')})`;
});
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['pie-card__circle']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__legend-item']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__body']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card pie-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
(__VLS_ctx.title);
const __VLS_0 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
    ...{ class: "muted-text" },
}));
const __VLS_2 = __VLS_1({
    ...{ class: "muted-text" },
}, ...__VLS_functionalComponentArgsRest(__VLS_1));
__VLS_3.slots.default;
const __VLS_4 = {}.InfoFilled;
/** @type {[typeof __VLS_components.InfoFilled, ]} */ ;
// @ts-ignore
const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({}));
const __VLS_6 = __VLS_5({}, ...__VLS_functionalComponentArgsRest(__VLS_5));
var __VLS_3;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "pie-card__body" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "pie-card__chart" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "pie-card__circle" },
    ...{ style: ({ background: __VLS_ctx.pieBackground }) },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "pie-card__center" },
});
(__VLS_ctx.centerLabel);
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "pie-card__legend" },
});
for (const [segment] of __VLS_getVForSourceType((__VLS_ctx.segments))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        key: (segment.label),
        ...{ class: "pie-card__legend-item" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "dot" },
        ...{ style: ({ background: segment.color }) },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (segment.label);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
    (segment.value);
}
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['muted-text']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__body']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__chart']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__circle']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__center']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__legend']} */ ;
/** @type {__VLS_StyleScopedClasses['pie-card__legend-item']} */ ;
/** @type {__VLS_StyleScopedClasses['dot']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            InfoFilled: InfoFilled,
            pieBackground: pieBackground,
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
