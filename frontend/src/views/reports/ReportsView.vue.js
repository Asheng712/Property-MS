import { ElMessage } from 'element-plus';
import PageContainer from '@/components/PageContainer.vue';
import { reportActions, reportCards } from '@/mock/data';
function runAction(title) {
    ElMessage.success(`${title} 已开始执行`);
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['report-task']} */ ;
/** @type {__VLS_StyleScopedClasses['report-task__text']} */ ;
/** @type {__VLS_StyleScopedClasses['report-task__text']} */ ;
/** @type {__VLS_StyleScopedClasses['reports-grid']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "报表导出与导入",
    description: "统一管理财务导出、资产导入和周报推送任务，减少重复人工整理工作。",
}));
const __VLS_1 = __VLS_0({
    title: "报表导出与导入",
    description: "统一管理财务导出、资产导入和周报推送任务，减少重复人工整理工作。",
}, ...__VLS_functionalComponentArgsRest(__VLS_0));
var __VLS_3 = {};
__VLS_2.slots.default;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "reports-grid" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
for (const [card] of __VLS_getVForSourceType((__VLS_ctx.reportCards))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
        key: (card.id),
        ...{ class: "report-task" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "report-task__text" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
    (card.title);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (card.subtitle);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "report-task__meta" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (card.metric);
    const __VLS_4 = {}.ElProgress;
    /** @type {[typeof __VLS_components.ElProgress, typeof __VLS_components.elProgress, ]} */ ;
    // @ts-ignore
    const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({
        percentage: (card.progress),
    }));
    const __VLS_6 = __VLS_5({
        percentage: (card.progress),
    }, ...__VLS_functionalComponentArgsRest(__VLS_5));
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "quick-actions" },
});
for (const [action] of __VLS_getVForSourceType((__VLS_ctx.reportActions))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
        ...{ onClick: (...[$event]) => {
                __VLS_ctx.runAction(action.title);
            } },
        key: (action.id),
        type: "button",
        ...{ class: "quick-actions__item" },
    });
    const __VLS_8 = {}.ElIcon;
    /** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
    // @ts-ignore
    const __VLS_9 = __VLS_asFunctionalComponent(__VLS_8, new __VLS_8({
        size: (24),
    }));
    const __VLS_10 = __VLS_9({
        size: (24),
    }, ...__VLS_functionalComponentArgsRest(__VLS_9));
    __VLS_11.slots.default;
    const __VLS_12 = ((action.icon));
    // @ts-ignore
    const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({}));
    const __VLS_14 = __VLS_13({}, ...__VLS_functionalComponentArgsRest(__VLS_13));
    var __VLS_11;
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (action.title);
}
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['reports-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['report-task']} */ ;
/** @type {__VLS_StyleScopedClasses['report-task__text']} */ ;
/** @type {__VLS_StyleScopedClasses['report-task__meta']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['quick-actions']} */ ;
/** @type {__VLS_StyleScopedClasses['quick-actions__item']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            reportActions: reportActions,
            reportCards: reportCards,
            runAction: runAction,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
