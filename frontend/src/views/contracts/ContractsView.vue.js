import { computed, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import DataToolbar from '@/components/DataToolbar.vue';
import PageContainer from '@/components/PageContainer.vue';
import { contractRecords } from '@/mock/data';
const keyword = ref('');
const status = ref('');
const dialogVisible = ref(false);
const filters = [
    { label: '生效中', value: 'active' },
    { label: '即将到期', value: 'expiring' },
    { label: '草稿', value: 'draft' },
];
const statusText = {
    active: '生效中',
    expiring: '即将到期',
    draft: '草稿',
};
const statusClass = {
    active: 'success',
    expiring: 'warning',
    draft: 'info',
};
const draft = reactive({
    contractNo: '',
    tenant: '',
    asset: '',
});
const filteredContracts = computed(() => contractRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.contractNo}${item.tenant}${item.asset}`.includes(keyword.value);
    const matchesStatus = !status.value || item.status === status.value;
    return matchesKeyword && matchesStatus;
}));
function createContract() {
    dialogVisible.value = false;
    ElMessage.success(`合同 ${draft.contractNo || '草稿'} 已创建`);
    draft.contractNo = '';
    draft.tenant = '';
    draft.asset = '';
}
function getStatusText(statusValue) {
    return statusText[statusValue];
}
function getStatusClass(statusValue) {
    return statusClass[statusValue];
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "商业合同管理",
    description: "统一维护租赁合同、到期提醒和租金金额，支持新增草稿与续签跟进。",
}));
const __VLS_1 = __VLS_0({
    title: "商业合同管理",
    description: "统一维护租赁合同、到期提醒和租金金额，支持新增草稿与续签跟进。",
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
            __VLS_ctx.dialogVisible = true;
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
    placeholder: "搜索合同编号、租户...",
    filters: (__VLS_ctx.filters),
}));
const __VLS_13 = __VLS_12({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索合同编号、租户...",
    filters: (__VLS_ctx.filters),
}, ...__VLS_functionalComponentArgsRest(__VLS_12));
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
const __VLS_15 = {}.ElTable;
/** @type {[typeof __VLS_components.ElTable, typeof __VLS_components.elTable, typeof __VLS_components.ElTable, typeof __VLS_components.elTable, ]} */ ;
// @ts-ignore
const __VLS_16 = __VLS_asFunctionalComponent(__VLS_15, new __VLS_15({
    data: (__VLS_ctx.filteredContracts),
}));
const __VLS_17 = __VLS_16({
    data: (__VLS_ctx.filteredContracts),
}, ...__VLS_functionalComponentArgsRest(__VLS_16));
__VLS_18.slots.default;
const __VLS_19 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_20 = __VLS_asFunctionalComponent(__VLS_19, new __VLS_19({
    prop: "contractNo",
    label: "合同编号",
    minWidth: "150",
}));
const __VLS_21 = __VLS_20({
    prop: "contractNo",
    label: "合同编号",
    minWidth: "150",
}, ...__VLS_functionalComponentArgsRest(__VLS_20));
const __VLS_23 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_24 = __VLS_asFunctionalComponent(__VLS_23, new __VLS_23({
    prop: "tenant",
    label: "租户名称",
    minWidth: "140",
}));
const __VLS_25 = __VLS_24({
    prop: "tenant",
    label: "租户名称",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_24));
const __VLS_27 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_28 = __VLS_asFunctionalComponent(__VLS_27, new __VLS_27({
    prop: "asset",
    label: "关联资产",
    minWidth: "120",
}));
const __VLS_29 = __VLS_28({
    prop: "asset",
    label: "关联资产",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_28));
const __VLS_31 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_32 = __VLS_asFunctionalComponent(__VLS_31, new __VLS_31({
    prop: "period",
    label: "合同周期",
    minWidth: "220",
}));
const __VLS_33 = __VLS_32({
    prop: "period",
    label: "合同周期",
    minWidth: "220",
}, ...__VLS_functionalComponentArgsRest(__VLS_32));
const __VLS_35 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_36 = __VLS_asFunctionalComponent(__VLS_35, new __VLS_35({
    label: "合同金额",
    minWidth: "140",
}));
const __VLS_37 = __VLS_36({
    label: "合同金额",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_36));
__VLS_38.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_38.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    (row.amount.toLocaleString());
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
    const __VLS_47 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_48 = __VLS_asFunctionalComponent(__VLS_47, new __VLS_47({
        link: true,
        type: "primary",
    }));
    const __VLS_49 = __VLS_48({
        link: true,
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_48));
    __VLS_50.slots.default;
    var __VLS_50;
}
var __VLS_46;
var __VLS_18;
const __VLS_51 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_52 = __VLS_asFunctionalComponent(__VLS_51, new __VLS_51({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新增商业合同",
    width: "520px",
}));
const __VLS_53 = __VLS_52({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新增商业合同",
    width: "520px",
}, ...__VLS_functionalComponentArgsRest(__VLS_52));
__VLS_54.slots.default;
const __VLS_55 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_56 = __VLS_asFunctionalComponent(__VLS_55, new __VLS_55({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_57 = __VLS_56({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_56));
__VLS_58.slots.default;
const __VLS_59 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_60 = __VLS_asFunctionalComponent(__VLS_59, new __VLS_59({
    label: "合同编号",
}));
const __VLS_61 = __VLS_60({
    label: "合同编号",
}, ...__VLS_functionalComponentArgsRest(__VLS_60));
__VLS_62.slots.default;
const __VLS_63 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_64 = __VLS_asFunctionalComponent(__VLS_63, new __VLS_63({
    modelValue: (__VLS_ctx.draft.contractNo),
}));
const __VLS_65 = __VLS_64({
    modelValue: (__VLS_ctx.draft.contractNo),
}, ...__VLS_functionalComponentArgsRest(__VLS_64));
var __VLS_62;
const __VLS_67 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_68 = __VLS_asFunctionalComponent(__VLS_67, new __VLS_67({
    label: "租户名称",
}));
const __VLS_69 = __VLS_68({
    label: "租户名称",
}, ...__VLS_functionalComponentArgsRest(__VLS_68));
__VLS_70.slots.default;
const __VLS_71 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_72 = __VLS_asFunctionalComponent(__VLS_71, new __VLS_71({
    modelValue: (__VLS_ctx.draft.tenant),
}));
const __VLS_73 = __VLS_72({
    modelValue: (__VLS_ctx.draft.tenant),
}, ...__VLS_functionalComponentArgsRest(__VLS_72));
var __VLS_70;
const __VLS_75 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_76 = __VLS_asFunctionalComponent(__VLS_75, new __VLS_75({
    label: "关联资产",
}));
const __VLS_77 = __VLS_76({
    label: "关联资产",
}, ...__VLS_functionalComponentArgsRest(__VLS_76));
__VLS_78.slots.default;
const __VLS_79 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_80 = __VLS_asFunctionalComponent(__VLS_79, new __VLS_79({
    modelValue: (__VLS_ctx.draft.asset),
}));
const __VLS_81 = __VLS_80({
    modelValue: (__VLS_ctx.draft.asset),
}, ...__VLS_functionalComponentArgsRest(__VLS_80));
var __VLS_78;
var __VLS_58;
{
    const { footer: __VLS_thisSlot } = __VLS_54.slots;
    const __VLS_83 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_84 = __VLS_asFunctionalComponent(__VLS_83, new __VLS_83({
        ...{ 'onClick': {} },
    }));
    const __VLS_85 = __VLS_84({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_84));
    let __VLS_87;
    let __VLS_88;
    let __VLS_89;
    const __VLS_90 = {
        onClick: (...[$event]) => {
            __VLS_ctx.dialogVisible = false;
        }
    };
    __VLS_86.slots.default;
    var __VLS_86;
    const __VLS_91 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_92 = __VLS_asFunctionalComponent(__VLS_91, new __VLS_91({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_93 = __VLS_92({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_92));
    let __VLS_95;
    let __VLS_96;
    let __VLS_97;
    const __VLS_98 = {
        onClick: (__VLS_ctx.createContract)
    };
    __VLS_94.slots.default;
    var __VLS_94;
}
var __VLS_54;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            DataToolbar: DataToolbar,
            PageContainer: PageContainer,
            keyword: keyword,
            status: status,
            dialogVisible: dialogVisible,
            filters: filters,
            draft: draft,
            filteredContracts: filteredContracts,
            createContract: createContract,
            getStatusText: getStatusText,
            getStatusClass: getStatusClass,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
