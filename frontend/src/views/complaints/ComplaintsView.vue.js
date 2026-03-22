import { computed, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import DataToolbar from '@/components/DataToolbar.vue';
import PageContainer from '@/components/PageContainer.vue';
import { complaintRecords } from '@/mock/data';
const keyword = ref('');
const status = ref('');
const feedbackDrawer = ref(false);
const filters = [
    { label: '新建', value: 'new' },
    { label: '跟进中', value: 'following' },
    { label: '已关闭', value: 'closed' },
];
const statusText = {
    new: '新建',
    following: '跟进中',
    closed: '已关闭',
};
const statusClass = {
    new: 'warning',
    following: 'info',
    closed: 'success',
};
const priorityText = {
    high: '高优先级',
    medium: '中优先级',
    low: '低优先级',
};
const priorityClass = {
    high: 'danger',
    medium: 'warning',
    low: 'info',
};
const feedback = reactive({
    status: 'resolved',
    note: '',
});
const filteredComplaints = computed(() => complaintRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.resident}${item.topic}`.includes(keyword.value);
    const matchesStatus = !status.value || item.status === status.value;
    return matchesKeyword && matchesStatus;
}));
function openComplaint(id) {
    feedbackDrawer.value = true;
    ElMessage.info(`正在处理投诉 ${id}`);
}
function submitFeedback() {
    feedbackDrawer.value = false;
    ElMessage.success('投诉回访记录已保存');
}
function getStatusText(statusValue) {
    return statusText[statusValue];
}
function getStatusClass(statusValue) {
    return statusClass[statusValue];
}
function getPriorityText(priorityValue) {
    return priorityText[priorityValue];
}
function getPriorityClass(priorityValue) {
    return priorityClass[priorityValue];
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "投诉建议闭环",
    description: "统一追踪投诉来源、优先级、处理进度和回访结果，避免问题在跨部门之间丢失。",
}));
const __VLS_1 = __VLS_0({
    title: "投诉建议闭环",
    description: "统一追踪投诉来源、优先级、处理进度和回访结果，避免问题在跨部门之间丢失。",
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
            __VLS_ctx.feedbackDrawer = true;
        }
    };
    __VLS_7.slots.default;
    var __VLS_7;
}
/** @type {[typeof DataToolbar, ]} */ ;
// @ts-ignore
const __VLS_12 = __VLS_asFunctionalComponent(DataToolbar, new DataToolbar({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索投诉人、主题...",
    filters: (__VLS_ctx.filters),
}));
const __VLS_13 = __VLS_12({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索投诉人、主题...",
    filters: (__VLS_ctx.filters),
}, ...__VLS_functionalComponentArgsRest(__VLS_12));
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
const __VLS_15 = {}.ElTable;
/** @type {[typeof __VLS_components.ElTable, typeof __VLS_components.elTable, typeof __VLS_components.ElTable, typeof __VLS_components.elTable, ]} */ ;
// @ts-ignore
const __VLS_16 = __VLS_asFunctionalComponent(__VLS_15, new __VLS_15({
    data: (__VLS_ctx.filteredComplaints),
}));
const __VLS_17 = __VLS_16({
    data: (__VLS_ctx.filteredComplaints),
}, ...__VLS_functionalComponentArgsRest(__VLS_16));
__VLS_18.slots.default;
const __VLS_19 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_20 = __VLS_asFunctionalComponent(__VLS_19, new __VLS_19({
    prop: "id",
    label: "编号",
    minWidth: "120",
}));
const __VLS_21 = __VLS_20({
    prop: "id",
    label: "编号",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_20));
const __VLS_23 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_24 = __VLS_asFunctionalComponent(__VLS_23, new __VLS_23({
    prop: "resident",
    label: "投诉人",
    minWidth: "120",
}));
const __VLS_25 = __VLS_24({
    prop: "resident",
    label: "投诉人",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_24));
const __VLS_27 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_28 = __VLS_asFunctionalComponent(__VLS_27, new __VLS_27({
    prop: "topic",
    label: "投诉主题",
    minWidth: "220",
}));
const __VLS_29 = __VLS_28({
    prop: "topic",
    label: "投诉主题",
    minWidth: "220",
}, ...__VLS_functionalComponentArgsRest(__VLS_28));
const __VLS_31 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_32 = __VLS_asFunctionalComponent(__VLS_31, new __VLS_31({
    prop: "createdAt",
    label: "提交时间",
    minWidth: "160",
}));
const __VLS_33 = __VLS_32({
    prop: "createdAt",
    label: "提交时间",
    minWidth: "160",
}, ...__VLS_functionalComponentArgsRest(__VLS_32));
const __VLS_35 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_36 = __VLS_asFunctionalComponent(__VLS_35, new __VLS_35({
    label: "优先级",
    minWidth: "110",
}));
const __VLS_37 = __VLS_36({
    label: "优先级",
    minWidth: "110",
}, ...__VLS_functionalComponentArgsRest(__VLS_36));
__VLS_38.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_38.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (__VLS_ctx.getPriorityClass(row.priority)) },
    });
    (__VLS_ctx.getPriorityText(row.priority));
}
var __VLS_38;
const __VLS_39 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_40 = __VLS_asFunctionalComponent(__VLS_39, new __VLS_39({
    label: "状态",
    minWidth: "120",
}));
const __VLS_41 = __VLS_40({
    label: "状态",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_40));
__VLS_42.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_42.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (__VLS_ctx.getStatusClass(row.status)) },
    });
    (__VLS_ctx.getStatusText(row.status));
}
var __VLS_42;
const __VLS_43 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_44 = __VLS_asFunctionalComponent(__VLS_43, new __VLS_43({
    label: "操作",
    width: "120",
}));
const __VLS_45 = __VLS_44({
    label: "操作",
    width: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_44));
__VLS_46.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_46.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    const __VLS_47 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_48 = __VLS_asFunctionalComponent(__VLS_47, new __VLS_47({
        ...{ 'onClick': {} },
        link: true,
        type: "primary",
    }));
    const __VLS_49 = __VLS_48({
        ...{ 'onClick': {} },
        link: true,
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_48));
    let __VLS_51;
    let __VLS_52;
    let __VLS_53;
    const __VLS_54 = {
        onClick: (...[$event]) => {
            __VLS_ctx.openComplaint(row.id);
        }
    };
    __VLS_50.slots.default;
    var __VLS_50;
}
var __VLS_46;
var __VLS_18;
const __VLS_55 = {}.ElDrawer;
/** @type {[typeof __VLS_components.ElDrawer, typeof __VLS_components.elDrawer, typeof __VLS_components.ElDrawer, typeof __VLS_components.elDrawer, ]} */ ;
// @ts-ignore
const __VLS_56 = __VLS_asFunctionalComponent(__VLS_55, new __VLS_55({
    modelValue: (__VLS_ctx.feedbackDrawer),
    title: "投诉回访记录",
    size: "420px",
}));
const __VLS_57 = __VLS_56({
    modelValue: (__VLS_ctx.feedbackDrawer),
    title: "投诉回访记录",
    size: "420px",
}, ...__VLS_functionalComponentArgsRest(__VLS_56));
__VLS_58.slots.default;
const __VLS_59 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_60 = __VLS_asFunctionalComponent(__VLS_59, new __VLS_59({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_61 = __VLS_60({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_60));
__VLS_62.slots.default;
const __VLS_63 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_64 = __VLS_asFunctionalComponent(__VLS_63, new __VLS_63({
    label: "回访结果",
}));
const __VLS_65 = __VLS_64({
    label: "回访结果",
}, ...__VLS_functionalComponentArgsRest(__VLS_64));
__VLS_66.slots.default;
const __VLS_67 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_68 = __VLS_asFunctionalComponent(__VLS_67, new __VLS_67({
    modelValue: (__VLS_ctx.feedback.status),
}));
const __VLS_69 = __VLS_68({
    modelValue: (__VLS_ctx.feedback.status),
}, ...__VLS_functionalComponentArgsRest(__VLS_68));
__VLS_70.slots.default;
const __VLS_71 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_72 = __VLS_asFunctionalComponent(__VLS_71, new __VLS_71({
    label: "已解决",
    value: "resolved",
}));
const __VLS_73 = __VLS_72({
    label: "已解决",
    value: "resolved",
}, ...__VLS_functionalComponentArgsRest(__VLS_72));
const __VLS_75 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_76 = __VLS_asFunctionalComponent(__VLS_75, new __VLS_75({
    label: "待跟进",
    value: "pending",
}));
const __VLS_77 = __VLS_76({
    label: "待跟进",
    value: "pending",
}, ...__VLS_functionalComponentArgsRest(__VLS_76));
var __VLS_70;
var __VLS_66;
const __VLS_79 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_80 = __VLS_asFunctionalComponent(__VLS_79, new __VLS_79({
    label: "回访说明",
}));
const __VLS_81 = __VLS_80({
    label: "回访说明",
}, ...__VLS_functionalComponentArgsRest(__VLS_80));
__VLS_82.slots.default;
const __VLS_83 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_84 = __VLS_asFunctionalComponent(__VLS_83, new __VLS_83({
    modelValue: (__VLS_ctx.feedback.note),
    type: "textarea",
    rows: "6",
}));
const __VLS_85 = __VLS_84({
    modelValue: (__VLS_ctx.feedback.note),
    type: "textarea",
    rows: "6",
}, ...__VLS_functionalComponentArgsRest(__VLS_84));
var __VLS_82;
var __VLS_62;
{
    const { footer: __VLS_thisSlot } = __VLS_58.slots;
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "drawer-footer" },
    });
    const __VLS_87 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_88 = __VLS_asFunctionalComponent(__VLS_87, new __VLS_87({
        ...{ 'onClick': {} },
    }));
    const __VLS_89 = __VLS_88({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_88));
    let __VLS_91;
    let __VLS_92;
    let __VLS_93;
    const __VLS_94 = {
        onClick: (...[$event]) => {
            __VLS_ctx.feedbackDrawer = false;
        }
    };
    __VLS_90.slots.default;
    var __VLS_90;
    const __VLS_95 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_96 = __VLS_asFunctionalComponent(__VLS_95, new __VLS_95({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_97 = __VLS_96({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_96));
    let __VLS_99;
    let __VLS_100;
    let __VLS_101;
    const __VLS_102 = {
        onClick: (__VLS_ctx.submitFeedback)
    };
    __VLS_98.slots.default;
    var __VLS_98;
}
var __VLS_58;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
/** @type {__VLS_StyleScopedClasses['drawer-footer']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            DataToolbar: DataToolbar,
            PageContainer: PageContainer,
            keyword: keyword,
            status: status,
            feedbackDrawer: feedbackDrawer,
            filters: filters,
            feedback: feedback,
            filteredComplaints: filteredComplaints,
            openComplaint: openComplaint,
            submitFeedback: submitFeedback,
            getStatusText: getStatusText,
            getStatusClass: getStatusClass,
            getPriorityText: getPriorityText,
            getPriorityClass: getPriorityClass,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
