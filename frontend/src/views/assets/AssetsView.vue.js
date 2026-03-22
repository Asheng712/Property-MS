import { computed, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import DataToolbar from '@/components/DataToolbar.vue';
import PageContainer from '@/components/PageContainer.vue';
import { assetRecords, assetTree } from '@/mock/data';
const keyword = ref('');
const status = ref('');
const selectedZone = ref('');
const assetDialogVisible = ref(false);
const statusFilters = [
    { label: '经营中', value: 'occupied' },
    { label: '空置', value: 'vacant' },
    { label: '已售', value: 'sold' },
];
const assetStatusText = {
    occupied: '经营中',
    vacant: '空置',
    sold: '已售',
};
const assetStatusClass = {
    occupied: 'success',
    vacant: 'warning',
    sold: 'info',
};
const categoryText = {
    shop: '商铺',
    residence: '住宅',
    parking: '车位',
};
const draftAsset = reactive({
    code: '',
    category: 'shop',
    area: '',
});
const filteredAssets = computed(() => assetRecords.filter((item) => {
    const matchesZone = !selectedZone.value || item.zone === selectedZone.value || item.id === selectedZone.value;
    const matchesKeyword = !keyword.value || `${item.code}${item.occupant}`.includes(keyword.value);
    const matchesStatus = !status.value || item.deliveryStatus === status.value;
    return matchesZone && matchesKeyword && matchesStatus;
}));
function handleNodeClick(node) {
    selectedZone.value = node.id.includes('-') ? node.id.split('-')[0] : node.id;
}
function openEdit(code) {
    ElMessage.success(`已打开 ${code} 的编辑弹窗占位，可继续接后端接口。`);
}
function getCategoryText(category) {
    return categoryText[category];
}
function getAssetStatusText(statusValue) {
    return assetStatusText[statusValue];
}
function getAssetStatusClass(statusValue) {
    return assetStatusClass[statusValue];
}
function saveAsset() {
    if (!draftAsset.code.trim()) {
        ElMessage.warning('请先填写资产编号');
        return;
    }
    assetDialogVisible.value = false;
    ElMessage.success('资产已保存到本地演示态');
    draftAsset.code = '';
    draftAsset.category = 'shop';
    draftAsset.area = '';
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['asset-layout']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "资产全景管理",
    description: "以树形结构管理楼栋、单元、房间及商铺，基础台账一目了然。",
}));
const __VLS_1 = __VLS_0({
    title: "资产全景管理",
    description: "以树形结构管理楼栋、单元、房间及商铺，基础台账一目了然。",
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
            __VLS_ctx.assetDialogVisible = true;
        }
    };
    __VLS_7.slots.default;
    var __VLS_7;
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "asset-layout" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card tree-panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
const __VLS_12 = {}.ElTree;
/** @type {[typeof __VLS_components.ElTree, typeof __VLS_components.elTree, ]} */ ;
// @ts-ignore
const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
    ...{ 'onNodeClick': {} },
    nodeKey: "id",
    data: (__VLS_ctx.assetTree),
    defaultExpandAll: true,
    expandOnClickNode: (false),
}));
const __VLS_14 = __VLS_13({
    ...{ 'onNodeClick': {} },
    nodeKey: "id",
    data: (__VLS_ctx.assetTree),
    defaultExpandAll: true,
    expandOnClickNode: (false),
}, ...__VLS_functionalComponentArgsRest(__VLS_13));
let __VLS_16;
let __VLS_17;
let __VLS_18;
const __VLS_19 = {
    onNodeClick: (__VLS_ctx.handleNodeClick)
};
var __VLS_15;
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "asset-main" },
});
/** @type {[typeof DataToolbar, ]} */ ;
// @ts-ignore
const __VLS_20 = __VLS_asFunctionalComponent(DataToolbar, new DataToolbar({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索房间号、业主姓名...",
    selectPlaceholder: "筛选状态",
    filters: (__VLS_ctx.statusFilters),
}));
const __VLS_21 = __VLS_20({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索房间号、业主姓名...",
    selectPlaceholder: "筛选状态",
    filters: (__VLS_ctx.statusFilters),
}, ...__VLS_functionalComponentArgsRest(__VLS_20));
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
const __VLS_23 = {}.ElTable;
/** @type {[typeof __VLS_components.ElTable, typeof __VLS_components.elTable, typeof __VLS_components.ElTable, typeof __VLS_components.elTable, ]} */ ;
// @ts-ignore
const __VLS_24 = __VLS_asFunctionalComponent(__VLS_23, new __VLS_23({
    data: (__VLS_ctx.filteredAssets),
}));
const __VLS_25 = __VLS_24({
    data: (__VLS_ctx.filteredAssets),
}, ...__VLS_functionalComponentArgsRest(__VLS_24));
__VLS_26.slots.default;
const __VLS_27 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_28 = __VLS_asFunctionalComponent(__VLS_27, new __VLS_27({
    prop: "code",
    label: "房号/铺号",
    minWidth: "120",
}));
const __VLS_29 = __VLS_28({
    prop: "code",
    label: "房号/铺号",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_28));
const __VLS_31 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_32 = __VLS_asFunctionalComponent(__VLS_31, new __VLS_31({
    prop: "name",
    label: "房间号/店铺号",
    minWidth: "140",
}));
const __VLS_33 = __VLS_32({
    prop: "name",
    label: "房间号/店铺号",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_32));
const __VLS_35 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_36 = __VLS_asFunctionalComponent(__VLS_35, new __VLS_35({
    label: "资源类型",
    minWidth: "120",
}));
const __VLS_37 = __VLS_36({
    label: "资源类型",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_36));
__VLS_38.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_38.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    (__VLS_ctx.getCategoryText(row.category));
}
var __VLS_38;
const __VLS_39 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_40 = __VLS_asFunctionalComponent(__VLS_39, new __VLS_39({
    label: "建筑面积",
    minWidth: "160",
}));
const __VLS_41 = __VLS_40({
    label: "建筑面积",
    minWidth: "160",
}, ...__VLS_functionalComponentArgsRest(__VLS_40));
__VLS_42.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_42.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    (row.area);
    (row.areaLabel);
}
var __VLS_42;
const __VLS_43 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_44 = __VLS_asFunctionalComponent(__VLS_43, new __VLS_43({
    label: "交付状态",
    minWidth: "120",
}));
const __VLS_45 = __VLS_44({
    label: "交付状态",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_44));
__VLS_46.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_46.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (__VLS_ctx.getAssetStatusClass(row.deliveryStatus)) },
    });
    (__VLS_ctx.getAssetStatusText(row.deliveryStatus));
}
var __VLS_46;
const __VLS_47 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_48 = __VLS_asFunctionalComponent(__VLS_47, new __VLS_47({
    prop: "occupant",
    label: "业主/承租人",
    minWidth: "140",
}));
const __VLS_49 = __VLS_48({
    prop: "occupant",
    label: "业主/承租人",
    minWidth: "140",
}, ...__VLS_functionalComponentArgsRest(__VLS_48));
const __VLS_51 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_52 = __VLS_asFunctionalComponent(__VLS_51, new __VLS_51({
    label: "操作",
    width: "100",
    fixed: "right",
}));
const __VLS_53 = __VLS_52({
    label: "操作",
    width: "100",
    fixed: "right",
}, ...__VLS_functionalComponentArgsRest(__VLS_52));
__VLS_54.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_54.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    const __VLS_55 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_56 = __VLS_asFunctionalComponent(__VLS_55, new __VLS_55({
        ...{ 'onClick': {} },
        link: true,
        type: "primary",
    }));
    const __VLS_57 = __VLS_56({
        ...{ 'onClick': {} },
        link: true,
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_56));
    let __VLS_59;
    let __VLS_60;
    let __VLS_61;
    const __VLS_62 = {
        onClick: (...[$event]) => {
            __VLS_ctx.openEdit(row.code);
        }
    };
    __VLS_58.slots.default;
    var __VLS_58;
}
var __VLS_54;
var __VLS_26;
const __VLS_63 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_64 = __VLS_asFunctionalComponent(__VLS_63, new __VLS_63({
    modelValue: (__VLS_ctx.assetDialogVisible),
    title: "新增资产",
    width: "520px",
}));
const __VLS_65 = __VLS_64({
    modelValue: (__VLS_ctx.assetDialogVisible),
    title: "新增资产",
    width: "520px",
}, ...__VLS_functionalComponentArgsRest(__VLS_64));
__VLS_66.slots.default;
const __VLS_67 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_68 = __VLS_asFunctionalComponent(__VLS_67, new __VLS_67({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_69 = __VLS_68({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_68));
__VLS_70.slots.default;
const __VLS_71 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_72 = __VLS_asFunctionalComponent(__VLS_71, new __VLS_71({
    label: "房号/铺号",
}));
const __VLS_73 = __VLS_72({
    label: "房号/铺号",
}, ...__VLS_functionalComponentArgsRest(__VLS_72));
__VLS_74.slots.default;
const __VLS_75 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_76 = __VLS_asFunctionalComponent(__VLS_75, new __VLS_75({
    modelValue: (__VLS_ctx.draftAsset.code),
}));
const __VLS_77 = __VLS_76({
    modelValue: (__VLS_ctx.draftAsset.code),
}, ...__VLS_functionalComponentArgsRest(__VLS_76));
var __VLS_74;
const __VLS_79 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_80 = __VLS_asFunctionalComponent(__VLS_79, new __VLS_79({
    label: "资源类型",
}));
const __VLS_81 = __VLS_80({
    label: "资源类型",
}, ...__VLS_functionalComponentArgsRest(__VLS_80));
__VLS_82.slots.default;
const __VLS_83 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_84 = __VLS_asFunctionalComponent(__VLS_83, new __VLS_83({
    modelValue: (__VLS_ctx.draftAsset.category),
}));
const __VLS_85 = __VLS_84({
    modelValue: (__VLS_ctx.draftAsset.category),
}, ...__VLS_functionalComponentArgsRest(__VLS_84));
__VLS_86.slots.default;
const __VLS_87 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_88 = __VLS_asFunctionalComponent(__VLS_87, new __VLS_87({
    label: "商铺",
    value: "shop",
}));
const __VLS_89 = __VLS_88({
    label: "商铺",
    value: "shop",
}, ...__VLS_functionalComponentArgsRest(__VLS_88));
const __VLS_91 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_92 = __VLS_asFunctionalComponent(__VLS_91, new __VLS_91({
    label: "住宅",
    value: "residence",
}));
const __VLS_93 = __VLS_92({
    label: "住宅",
    value: "residence",
}, ...__VLS_functionalComponentArgsRest(__VLS_92));
const __VLS_95 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_96 = __VLS_asFunctionalComponent(__VLS_95, new __VLS_95({
    label: "车位",
    value: "parking",
}));
const __VLS_97 = __VLS_96({
    label: "车位",
    value: "parking",
}, ...__VLS_functionalComponentArgsRest(__VLS_96));
var __VLS_86;
var __VLS_82;
const __VLS_99 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_100 = __VLS_asFunctionalComponent(__VLS_99, new __VLS_99({
    label: "建筑面积",
}));
const __VLS_101 = __VLS_100({
    label: "建筑面积",
}, ...__VLS_functionalComponentArgsRest(__VLS_100));
__VLS_102.slots.default;
const __VLS_103 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_104 = __VLS_asFunctionalComponent(__VLS_103, new __VLS_103({
    modelValue: (__VLS_ctx.draftAsset.area),
}));
const __VLS_105 = __VLS_104({
    modelValue: (__VLS_ctx.draftAsset.area),
}, ...__VLS_functionalComponentArgsRest(__VLS_104));
var __VLS_102;
var __VLS_70;
{
    const { footer: __VLS_thisSlot } = __VLS_66.slots;
    const __VLS_107 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_108 = __VLS_asFunctionalComponent(__VLS_107, new __VLS_107({
        ...{ 'onClick': {} },
    }));
    const __VLS_109 = __VLS_108({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_108));
    let __VLS_111;
    let __VLS_112;
    let __VLS_113;
    const __VLS_114 = {
        onClick: (...[$event]) => {
            __VLS_ctx.assetDialogVisible = false;
        }
    };
    __VLS_110.slots.default;
    var __VLS_110;
    const __VLS_115 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_116 = __VLS_asFunctionalComponent(__VLS_115, new __VLS_115({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_117 = __VLS_116({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_116));
    let __VLS_119;
    let __VLS_120;
    let __VLS_121;
    const __VLS_122 = {
        onClick: (__VLS_ctx.saveAsset)
    };
    __VLS_118.slots.default;
    var __VLS_118;
}
var __VLS_66;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['asset-layout']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['tree-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['asset-main']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            DataToolbar: DataToolbar,
            PageContainer: PageContainer,
            assetTree: assetTree,
            keyword: keyword,
            status: status,
            assetDialogVisible: assetDialogVisible,
            statusFilters: statusFilters,
            draftAsset: draftAsset,
            filteredAssets: filteredAssets,
            handleNodeClick: handleNodeClick,
            openEdit: openEdit,
            getCategoryText: getCategoryText,
            getAssetStatusText: getAssetStatusText,
            getAssetStatusClass: getAssetStatusClass,
            saveAsset: saveAsset,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
