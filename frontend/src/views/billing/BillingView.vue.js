import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import PageContainer from '@/components/PageContainer.vue';
import { billBatches } from '@/mock/data';
const ruleDialogVisible = ref(false);
const ruleForm = reactive({
    propertyMode: 'area',
    utilityRule: '0-100 度：0.56 元；100-200 度：0.68 元；200 度以上：0.82 元',
    cycle: 'monthly',
});
function generateMonthlyBills() {
    ElMessage.success('已触发本月账单生成任务，当前为演示态。');
}
function saveRules() {
    ruleDialogVisible.value = false;
    ElMessage.success('计费规则已保存');
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['billing-summary__card']} */ ;
/** @type {__VLS_StyleScopedClasses['billing-summary__card']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "智能账单引擎",
    description: "一键基于单价、面积或阶梯计费规则批量生成账单，批处理过程透明可追踪。",
}));
const __VLS_1 = __VLS_0({
    title: "智能账单引擎",
    description: "一键基于单价、面积或阶梯计费规则批量生成账单，批处理过程透明可追踪。",
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
        plain: true,
    }));
    const __VLS_6 = __VLS_5({
        ...{ 'onClick': {} },
        plain: true,
    }, ...__VLS_functionalComponentArgsRest(__VLS_5));
    let __VLS_8;
    let __VLS_9;
    let __VLS_10;
    const __VLS_11 = {
        onClick: (...[$event]) => {
            __VLS_ctx.ruleDialogVisible = true;
        }
    };
    __VLS_7.slots.default;
    var __VLS_7;
    const __VLS_12 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
        ...{ 'onClick': {} },
        type: "primary",
        ...{ class: "btn-primary-gradient" },
    }));
    const __VLS_14 = __VLS_13({
        ...{ 'onClick': {} },
        type: "primary",
        ...{ class: "btn-primary-gradient" },
    }, ...__VLS_functionalComponentArgsRest(__VLS_13));
    let __VLS_16;
    let __VLS_17;
    let __VLS_18;
    const __VLS_19 = {
        onClick: (__VLS_ctx.generateMonthlyBills)
    };
    __VLS_15.slots.default;
    var __VLS_15;
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "billing-summary" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
    ...{ class: "surface-card billing-summary__card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "billing-summary__icon" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
const __VLS_20 = {}.ElTable;
/** @type {[typeof __VLS_components.ElTable, typeof __VLS_components.elTable, typeof __VLS_components.ElTable, typeof __VLS_components.elTable, ]} */ ;
// @ts-ignore
const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
    data: (__VLS_ctx.billBatches),
}));
const __VLS_22 = __VLS_21({
    data: (__VLS_ctx.billBatches),
}, ...__VLS_functionalComponentArgsRest(__VLS_21));
__VLS_23.slots.default;
const __VLS_24 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_25 = __VLS_asFunctionalComponent(__VLS_24, new __VLS_24({
    prop: "batchNo",
    label: "生成批次号",
    minWidth: "150",
}));
const __VLS_26 = __VLS_25({
    prop: "batchNo",
    label: "生成批次号",
    minWidth: "150",
}, ...__VLS_functionalComponentArgsRest(__VLS_25));
const __VLS_28 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
    prop: "scope",
    label: "计费对象范围",
    minWidth: "180",
}));
const __VLS_30 = __VLS_29({
    prop: "scope",
    label: "计费对象范围",
    minWidth: "180",
}, ...__VLS_functionalComponentArgsRest(__VLS_29));
const __VLS_32 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_33 = __VLS_asFunctionalComponent(__VLS_32, new __VLS_32({
    prop: "feeType",
    label: "费用类型",
    minWidth: "140",
}));
const __VLS_34 = __VLS_33({
    prop: "feeType",
    label: "费用类型",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_33));
const __VLS_36 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    prop: "generatedCount",
    label: "生成数量",
    minWidth: "100",
}));
const __VLS_38 = __VLS_37({
    prop: "generatedCount",
    label: "生成数量",
    minWidth: "100",
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
const __VLS_40 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    label: "总金额",
    minWidth: "140",
}));
const __VLS_42 = __VLS_41({
    label: "总金额",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
__VLS_43.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_43.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    (row.totalAmount.toLocaleString());
}
var __VLS_43;
const __VLS_44 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    prop: "executedAt",
    label: "执行时间",
    minWidth: "150",
}));
const __VLS_46 = __VLS_45({
    prop: "executedAt",
    label: "执行时间",
    minWidth: "150",
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
const __VLS_48 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    label: "状态",
    minWidth: "120",
}));
const __VLS_50 = __VLS_49({
    label: "状态",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
__VLS_51.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_51.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (row.status === 'done' ? 'success' : 'info') },
    });
    (row.status === 'done' ? '已生成' : '处理中');
}
var __VLS_51;
const __VLS_52 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_53 = __VLS_asFunctionalComponent(__VLS_52, new __VLS_52({
    label: "操作",
    width: "120",
}));
const __VLS_54 = __VLS_53({
    label: "操作",
    width: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_53));
__VLS_55.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_55.slots;
    const __VLS_56 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_57 = __VLS_asFunctionalComponent(__VLS_56, new __VLS_56({
        link: true,
        type: "primary",
    }));
    const __VLS_58 = __VLS_57({
        link: true,
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_57));
    __VLS_59.slots.default;
    var __VLS_59;
}
var __VLS_55;
var __VLS_23;
const __VLS_60 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_61 = __VLS_asFunctionalComponent(__VLS_60, new __VLS_60({
    modelValue: (__VLS_ctx.ruleDialogVisible),
    title: "计费规则配置",
    width: "560px",
}));
const __VLS_62 = __VLS_61({
    modelValue: (__VLS_ctx.ruleDialogVisible),
    title: "计费规则配置",
    width: "560px",
}, ...__VLS_functionalComponentArgsRest(__VLS_61));
__VLS_63.slots.default;
const __VLS_64 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_65 = __VLS_asFunctionalComponent(__VLS_64, new __VLS_64({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_66 = __VLS_65({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_65));
__VLS_67.slots.default;
const __VLS_68 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_69 = __VLS_asFunctionalComponent(__VLS_68, new __VLS_68({
    label: "物业费计价方式",
}));
const __VLS_70 = __VLS_69({
    label: "物业费计价方式",
}, ...__VLS_functionalComponentArgsRest(__VLS_69));
__VLS_71.slots.default;
const __VLS_72 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_73 = __VLS_asFunctionalComponent(__VLS_72, new __VLS_72({
    modelValue: (__VLS_ctx.ruleForm.propertyMode),
}));
const __VLS_74 = __VLS_73({
    modelValue: (__VLS_ctx.ruleForm.propertyMode),
}, ...__VLS_functionalComponentArgsRest(__VLS_73));
__VLS_75.slots.default;
const __VLS_76 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_77 = __VLS_asFunctionalComponent(__VLS_76, new __VLS_76({
    label: "按面积计费",
    value: "area",
}));
const __VLS_78 = __VLS_77({
    label: "按面积计费",
    value: "area",
}, ...__VLS_functionalComponentArgsRest(__VLS_77));
const __VLS_80 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_81 = __VLS_asFunctionalComponent(__VLS_80, new __VLS_80({
    label: "固定费用",
    value: "fixed",
}));
const __VLS_82 = __VLS_81({
    label: "固定费用",
    value: "fixed",
}, ...__VLS_functionalComponentArgsRest(__VLS_81));
var __VLS_75;
var __VLS_71;
const __VLS_84 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_85 = __VLS_asFunctionalComponent(__VLS_84, new __VLS_84({
    label: "水电阶梯规则",
}));
const __VLS_86 = __VLS_85({
    label: "水电阶梯规则",
}, ...__VLS_functionalComponentArgsRest(__VLS_85));
__VLS_87.slots.default;
const __VLS_88 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_89 = __VLS_asFunctionalComponent(__VLS_88, new __VLS_88({
    modelValue: (__VLS_ctx.ruleForm.utilityRule),
    type: "textarea",
    rows: "4",
}));
const __VLS_90 = __VLS_89({
    modelValue: (__VLS_ctx.ruleForm.utilityRule),
    type: "textarea",
    rows: "4",
}, ...__VLS_functionalComponentArgsRest(__VLS_89));
var __VLS_87;
const __VLS_92 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_93 = __VLS_asFunctionalComponent(__VLS_92, new __VLS_92({
    label: "生成周期",
}));
const __VLS_94 = __VLS_93({
    label: "生成周期",
}, ...__VLS_functionalComponentArgsRest(__VLS_93));
__VLS_95.slots.default;
const __VLS_96 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_97 = __VLS_asFunctionalComponent(__VLS_96, new __VLS_96({
    modelValue: (__VLS_ctx.ruleForm.cycle),
}));
const __VLS_98 = __VLS_97({
    modelValue: (__VLS_ctx.ruleForm.cycle),
}, ...__VLS_functionalComponentArgsRest(__VLS_97));
__VLS_99.slots.default;
const __VLS_100 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_101 = __VLS_asFunctionalComponent(__VLS_100, new __VLS_100({
    label: "每月 1 日",
    value: "monthly",
}));
const __VLS_102 = __VLS_101({
    label: "每月 1 日",
    value: "monthly",
}, ...__VLS_functionalComponentArgsRest(__VLS_101));
const __VLS_104 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_105 = __VLS_asFunctionalComponent(__VLS_104, new __VLS_104({
    label: "每季度首月 1 日",
    value: "seasonal",
}));
const __VLS_106 = __VLS_105({
    label: "每季度首月 1 日",
    value: "seasonal",
}, ...__VLS_functionalComponentArgsRest(__VLS_105));
var __VLS_99;
var __VLS_95;
var __VLS_67;
{
    const { footer: __VLS_thisSlot } = __VLS_63.slots;
    const __VLS_108 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_109 = __VLS_asFunctionalComponent(__VLS_108, new __VLS_108({
        ...{ 'onClick': {} },
    }));
    const __VLS_110 = __VLS_109({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_109));
    let __VLS_112;
    let __VLS_113;
    let __VLS_114;
    const __VLS_115 = {
        onClick: (...[$event]) => {
            __VLS_ctx.ruleDialogVisible = false;
        }
    };
    __VLS_111.slots.default;
    var __VLS_111;
    const __VLS_116 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_117 = __VLS_asFunctionalComponent(__VLS_116, new __VLS_116({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_118 = __VLS_117({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_117));
    let __VLS_120;
    let __VLS_121;
    let __VLS_122;
    const __VLS_123 = {
        onClick: (__VLS_ctx.saveRules)
    };
    __VLS_119.slots.default;
    var __VLS_119;
}
var __VLS_63;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['billing-summary']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['billing-summary__card']} */ ;
/** @type {__VLS_StyleScopedClasses['billing-summary__icon']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            billBatches: billBatches,
            ruleDialogVisible: ruleDialogVisible,
            ruleForm: ruleForm,
            generateMonthlyBills: generateMonthlyBills,
            saveRules: saveRules,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
