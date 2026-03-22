import { Search } from '@element-plus/icons-vue';
import { computed } from 'vue';
const props = withDefaults(defineProps(), {
    status: '',
    placeholder: '请输入关键词',
    selectPlaceholder: '筛选状态',
    filters: () => [],
});
const emit = defineEmits();
const keywordModel = computed({
    get: () => props.keyword,
    set: (value) => emit('update:keyword', value),
});
const statusModel = computed({
    get: () => props.status ?? '',
    set: (value) => emit('update:status', value),
});
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_withDefaultsArg = (function (t) { return t; })({
    status: '',
    placeholder: '请输入关键词',
    selectPlaceholder: '筛选状态',
    filters: () => [],
});
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['toolbar__left']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar__left']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar__left']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "toolbar surface-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "toolbar__left" },
});
const __VLS_0 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
    modelValue: (__VLS_ctx.keywordModel),
    placeholder: (__VLS_ctx.placeholder),
    clearable: true,
}));
const __VLS_2 = __VLS_1({
    modelValue: (__VLS_ctx.keywordModel),
    placeholder: (__VLS_ctx.placeholder),
    clearable: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_1));
__VLS_3.slots.default;
{
    const { prefix: __VLS_thisSlot } = __VLS_3.slots;
    const __VLS_4 = {}.ElIcon;
    /** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
    // @ts-ignore
    const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({}));
    const __VLS_6 = __VLS_5({}, ...__VLS_functionalComponentArgsRest(__VLS_5));
    __VLS_7.slots.default;
    const __VLS_8 = {}.Search;
    /** @type {[typeof __VLS_components.Search, ]} */ ;
    // @ts-ignore
    const __VLS_9 = __VLS_asFunctionalComponent(__VLS_8, new __VLS_8({}));
    const __VLS_10 = __VLS_9({}, ...__VLS_functionalComponentArgsRest(__VLS_9));
    var __VLS_7;
}
var __VLS_3;
if (__VLS_ctx.filters.length) {
    const __VLS_12 = {}.ElSelect;
    /** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
    // @ts-ignore
    const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
        modelValue: (__VLS_ctx.statusModel),
        clearable: true,
        placeholder: (__VLS_ctx.selectPlaceholder),
    }));
    const __VLS_14 = __VLS_13({
        modelValue: (__VLS_ctx.statusModel),
        clearable: true,
        placeholder: (__VLS_ctx.selectPlaceholder),
    }, ...__VLS_functionalComponentArgsRest(__VLS_13));
    __VLS_15.slots.default;
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.filters))) {
        const __VLS_16 = {}.ElOption;
        /** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
        // @ts-ignore
        const __VLS_17 = __VLS_asFunctionalComponent(__VLS_16, new __VLS_16({
            key: (item.value),
            label: (item.label),
            value: (item.value),
        }));
        const __VLS_18 = __VLS_17({
            key: (item.value),
            label: (item.label),
            value: (item.value),
        }, ...__VLS_functionalComponentArgsRest(__VLS_17));
    }
    var __VLS_15;
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "toolbar__actions" },
});
var __VLS_20 = {};
/** @type {__VLS_StyleScopedClasses['toolbar']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar__left']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar__actions']} */ ;
// @ts-ignore
var __VLS_21 = __VLS_20;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            Search: Search,
            keywordModel: keywordModel,
            statusModel: statusModel,
        };
    },
    __typeEmits: {},
    __typeProps: {},
    props: {},
});
const __VLS_component = (await import('vue')).defineComponent({
    setup() {
        return {};
    },
    __typeEmits: {},
    __typeProps: {},
    props: {},
});
export default {};
; /* PartiallyEnd: #4569/main.vue */
