import { computed, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import PageContainer from '@/components/PageContainer.vue';
import { repairTickets } from '@/mock/data';
const ticketDialogVisible = ref(false);
const ticketDraft = reactive({
    title: '',
    location: '',
    reporter: '',
});
const columns = computed(() => [
    { key: 'todo', title: '待处理 (接单)', color: '#f97316', items: repairTickets.filter((item) => item.stage === 'todo') },
    { key: 'doing', title: '处理中 (派单维修)', color: '#3b82f6', items: repairTickets.filter((item) => item.stage === 'doing') },
    { key: 'done', title: '已办结 (回访评价)', color: '#10b981', items: repairTickets.filter((item) => item.stage === 'done') },
]);
function assignTicket(ticketId) {
    ElMessage.success(`工单 ${ticketId} 已派给维修工程部`);
}
function completeTicket(ticketId) {
    ElMessage.success(`工单 ${ticketId} 已更新为完成待回访`);
}
function createTicket() {
    ticketDialogVisible.value = false;
    ElMessage.success('人工工单已录入');
    ticketDraft.title = '';
    ticketDraft.location = '';
    ticketDraft.reporter = '';
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['kanban-column__header']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-ticket']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-ticket']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-ticket']} */ ;
/** @type {__VLS_StyleScopedClasses['ticket-age']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-grid']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "报修工单调度",
    description: "完整的接单、派单、反馈闭环流程看板，基于状态流转设计。",
}));
const __VLS_1 = __VLS_0({
    title: "报修工单调度",
    description: "完整的接单、派单、反馈闭环流程看板，基于状态流转设计。",
}, ...__VLS_functionalComponentArgsRest(__VLS_0));
var __VLS_3 = {};
__VLS_2.slots.default;
{
    const { actions: __VLS_thisSlot } = __VLS_2.slots;
    const __VLS_4 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({
        ...{ 'onClick': {} },
        type: "primary",
        ...{ class: "btn-primary-gradient" },
    }));
    const __VLS_6 = __VLS_5({
        ...{ 'onClick': {} },
        type: "primary",
        ...{ class: "btn-primary-gradient" },
    }, ...__VLS_functionalComponentArgsRest(__VLS_5));
    let __VLS_8;
    let __VLS_9;
    let __VLS_10;
    const __VLS_11 = {
        onClick: (...[$event]) => {
            __VLS_ctx.ticketDialogVisible = true;
        }
    };
    __VLS_7.slots.default;
    var __VLS_7;
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "kanban-grid" },
});
for (const [column] of __VLS_getVForSourceType((__VLS_ctx.columns))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
        key: (column.key),
        ...{ class: "surface-card kanban-column" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "kanban-column__header" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "kanban-column__dot" },
        ...{ style: ({ background: column.color }) },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
    (column.title);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "kanban-column__count" },
    });
    (column.items.length);
    for (const [ticket] of __VLS_getVForSourceType((column.items))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
            key: (ticket.id),
            ...{ class: "kanban-ticket glass-card" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "kanban-ticket__top" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
            ...{ class: "ticket-id" },
        });
        (ticket.id);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
            ...{ class: "ticket-age" },
            ...{ class: ({ warning: column.key === 'todo' }) },
        });
        (ticket.ageLabel);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
        (ticket.title);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
        (ticket.location);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.footer, __VLS_intrinsicElements.footer)({});
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
        (ticket.assignee ?? ticket.reporter);
        if (column.key === 'todo') {
            const __VLS_12 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
                ...{ 'onClick': {} },
                plain: true,
                size: "small",
            }));
            const __VLS_14 = __VLS_13({
                ...{ 'onClick': {} },
                plain: true,
                size: "small",
            }, ...__VLS_functionalComponentArgsRest(__VLS_13));
            let __VLS_16;
            let __VLS_17;
            let __VLS_18;
            const __VLS_19 = {
                onClick: (...[$event]) => {
                    if (!(column.key === 'todo'))
                        return;
                    __VLS_ctx.assignTicket(ticket.id);
                }
            };
            __VLS_15.slots.default;
            var __VLS_15;
        }
        else if (column.key === 'doing') {
            const __VLS_20 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
                ...{ 'onClick': {} },
                link: true,
                type: "primary",
            }));
            const __VLS_22 = __VLS_21({
                ...{ 'onClick': {} },
                link: true,
                type: "primary",
            }, ...__VLS_functionalComponentArgsRest(__VLS_21));
            let __VLS_24;
            let __VLS_25;
            let __VLS_26;
            const __VLS_27 = {
                onClick: (...[$event]) => {
                    if (!!(column.key === 'todo'))
                        return;
                    if (!(column.key === 'doing'))
                        return;
                    __VLS_ctx.completeTicket(ticket.id);
                }
            };
            __VLS_23.slots.default;
            var __VLS_23;
        }
        else {
            const __VLS_28 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
                link: true,
                type: "info",
            }));
            const __VLS_30 = __VLS_29({
                link: true,
                type: "info",
            }, ...__VLS_functionalComponentArgsRest(__VLS_29));
            __VLS_31.slots.default;
            var __VLS_31;
        }
    }
}
const __VLS_32 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_33 = __VLS_asFunctionalComponent(__VLS_32, new __VLS_32({
    modelValue: (__VLS_ctx.ticketDialogVisible),
    title: "录入人工工单",
    width: "520px",
}));
const __VLS_34 = __VLS_33({
    modelValue: (__VLS_ctx.ticketDialogVisible),
    title: "录入人工工单",
    width: "520px",
}, ...__VLS_functionalComponentArgsRest(__VLS_33));
__VLS_35.slots.default;
const __VLS_36 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_38 = __VLS_37({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
__VLS_39.slots.default;
const __VLS_40 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    label: "报修主题",
}));
const __VLS_42 = __VLS_41({
    label: "报修主题",
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
__VLS_43.slots.default;
const __VLS_44 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    modelValue: (__VLS_ctx.ticketDraft.title),
}));
const __VLS_46 = __VLS_45({
    modelValue: (__VLS_ctx.ticketDraft.title),
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
var __VLS_43;
const __VLS_48 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    label: "报修位置",
}));
const __VLS_50 = __VLS_49({
    label: "报修位置",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
__VLS_51.slots.default;
const __VLS_52 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_53 = __VLS_asFunctionalComponent(__VLS_52, new __VLS_52({
    modelValue: (__VLS_ctx.ticketDraft.location),
}));
const __VLS_54 = __VLS_53({
    modelValue: (__VLS_ctx.ticketDraft.location),
}, ...__VLS_functionalComponentArgsRest(__VLS_53));
var __VLS_51;
const __VLS_56 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_57 = __VLS_asFunctionalComponent(__VLS_56, new __VLS_56({
    label: "报修人",
}));
const __VLS_58 = __VLS_57({
    label: "报修人",
}, ...__VLS_functionalComponentArgsRest(__VLS_57));
__VLS_59.slots.default;
const __VLS_60 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_61 = __VLS_asFunctionalComponent(__VLS_60, new __VLS_60({
    modelValue: (__VLS_ctx.ticketDraft.reporter),
}));
const __VLS_62 = __VLS_61({
    modelValue: (__VLS_ctx.ticketDraft.reporter),
}, ...__VLS_functionalComponentArgsRest(__VLS_61));
var __VLS_59;
var __VLS_39;
{
    const { footer: __VLS_thisSlot } = __VLS_35.slots;
    const __VLS_64 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_65 = __VLS_asFunctionalComponent(__VLS_64, new __VLS_64({
        ...{ 'onClick': {} },
    }));
    const __VLS_66 = __VLS_65({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_65));
    let __VLS_68;
    let __VLS_69;
    let __VLS_70;
    const __VLS_71 = {
        onClick: (...[$event]) => {
            __VLS_ctx.ticketDialogVisible = false;
        }
    };
    __VLS_67.slots.default;
    var __VLS_67;
    const __VLS_72 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_73 = __VLS_asFunctionalComponent(__VLS_72, new __VLS_72({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_74 = __VLS_73({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_73));
    let __VLS_76;
    let __VLS_77;
    let __VLS_78;
    const __VLS_79 = {
        onClick: (__VLS_ctx.createTicket)
    };
    __VLS_75.slots.default;
    var __VLS_75;
}
var __VLS_35;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-column']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-column__header']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-column__dot']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-column__count']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-ticket']} */ ;
/** @type {__VLS_StyleScopedClasses['glass-card']} */ ;
/** @type {__VLS_StyleScopedClasses['kanban-ticket__top']} */ ;
/** @type {__VLS_StyleScopedClasses['ticket-id']} */ ;
/** @type {__VLS_StyleScopedClasses['ticket-age']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            ticketDialogVisible: ticketDialogVisible,
            ticketDraft: ticketDraft,
            columns: columns,
            assignTicket: assignTicket,
            completeTicket: completeTicket,
            createTicket: createTicket,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
