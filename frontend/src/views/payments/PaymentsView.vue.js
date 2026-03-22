import { computed, ref } from 'vue';
import { ElMessage } from 'element-plus';
import DataToolbar from '@/components/DataToolbar.vue';
import PageContainer from '@/components/PageContainer.vue';
import { paymentRecords } from '@/mock/data';
const keyword = ref('');
const status = ref('');
const filters = [
    { label: '已支付', value: 'paid' },
    { label: '待核销', value: 'pending' },
    { label: '退款中', value: 'refund' },
];
const statusText = {
    paid: '已支付',
    pending: '待核销',
    refund: '退款中',
};
const statusClass = {
    paid: 'success',
    pending: 'warning',
    refund: 'danger',
};
const filteredPayments = computed(() => paymentRecords.filter((item) => {
    const matchesKeyword = !keyword.value || `${item.billNo}${item.payer}${item.channel}`.includes(keyword.value);
    const matchesStatus = !status.value || item.status === status.value;
    return matchesKeyword && matchesStatus;
}));
function reconcile() {
    ElMessage.success('已提交批量核销请求');
}
function exportRecords() {
    ElMessage.success('缴费流水导出任务已加入队列');
}
function inspectRecord(billNo) {
    ElMessage.info(`查看 ${billNo} 的核销详情`);
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
    title: "缴费流水与核销",
    description: "聚合线上支付、人工核销和退款记录，方便财务快速对账与异常处理。",
}));
const __VLS_1 = __VLS_0({
    title: "缴费流水与核销",
    description: "聚合线上支付、人工核销和退款记录，方便财务快速对账与异常处理。",
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
        onClick: (__VLS_ctx.reconcile)
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
        onClick: (__VLS_ctx.exportRecords)
    };
    __VLS_15.slots.default;
    var __VLS_15;
}
/** @type {[typeof DataToolbar, ]} */ ;
// @ts-ignore
const __VLS_20 = __VLS_asFunctionalComponent(DataToolbar, new DataToolbar({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索账单编号、缴费人...",
    filters: (__VLS_ctx.filters),
}));
const __VLS_21 = __VLS_20({
    keyword: (__VLS_ctx.keyword),
    status: (__VLS_ctx.status),
    placeholder: "搜索账单编号、缴费人...",
    filters: (__VLS_ctx.filters),
}, ...__VLS_functionalComponentArgsRest(__VLS_20));
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card" },
});
const __VLS_23 = {}.ElTable;
/** @type {[typeof __VLS_components.ElTable, typeof __VLS_components.elTable, typeof __VLS_components.ElTable, typeof __VLS_components.elTable, ]} */ ;
// @ts-ignore
const __VLS_24 = __VLS_asFunctionalComponent(__VLS_23, new __VLS_23({
    data: (__VLS_ctx.filteredPayments),
}));
const __VLS_25 = __VLS_24({
    data: (__VLS_ctx.filteredPayments),
}, ...__VLS_functionalComponentArgsRest(__VLS_24));
__VLS_26.slots.default;
const __VLS_27 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_28 = __VLS_asFunctionalComponent(__VLS_27, new __VLS_27({
    prop: "billNo",
    label: "账单编号",
    minWidth: "160",
}));
const __VLS_29 = __VLS_28({
    prop: "billNo",
    label: "账单编号",
    minWidth: "160",
}, ...__VLS_functionalComponentArgsRest(__VLS_28));
const __VLS_31 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_32 = __VLS_asFunctionalComponent(__VLS_31, new __VLS_31({
    prop: "payer",
    label: "缴费人",
    minWidth: "130",
}));
const __VLS_33 = __VLS_32({
    prop: "payer",
    label: "缴费人",
    minWidth: "130",
}, ...__VLS_functionalComponentArgsRest(__VLS_32));
const __VLS_35 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_36 = __VLS_asFunctionalComponent(__VLS_35, new __VLS_35({
    label: "缴费金额",
    minWidth: "130",
}));
const __VLS_37 = __VLS_36({
    label: "缴费金额",
    minWidth: "130",
}, ...__VLS_functionalComponentArgsRest(__VLS_36));
__VLS_38.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_38.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    (row.amount.toLocaleString());
}
var __VLS_38;
const __VLS_39 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_40 = __VLS_asFunctionalComponent(__VLS_39, new __VLS_39({
    prop: "channel",
    label: "缴费渠道",
    minWidth: "130",
}));
const __VLS_41 = __VLS_40({
    prop: "channel",
    label: "缴费渠道",
    minWidth: "130",
}, ...__VLS_functionalComponentArgsRest(__VLS_40));
const __VLS_43 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_44 = __VLS_asFunctionalComponent(__VLS_43, new __VLS_43({
    prop: "paidAt",
    label: "时间",
    minWidth: "150",
}));
const __VLS_45 = __VLS_44({
    prop: "paidAt",
    label: "时间",
    minWidth: "150",
}, ...__VLS_functionalComponentArgsRest(__VLS_44));
const __VLS_47 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_48 = __VLS_asFunctionalComponent(__VLS_47, new __VLS_47({
    label: "状态",
    minWidth: "120",
}));
const __VLS_49 = __VLS_48({
    label: "状态",
    minWidth: "120",
}, ...__VLS_functionalComponentArgsRest(__VLS_48));
__VLS_50.slots.default;
{
    const { default: __VLS_thisSlot } = __VLS_50.slots;
    const [{ row }] = __VLS_getSlotParams(__VLS_thisSlot);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (__VLS_ctx.getStatusClass(row.status)) },
    });
    (__VLS_ctx.getStatusText(row.status));
}
var __VLS_50;
const __VLS_51 = {}.ElTableColumn;
/** @type {[typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, typeof __VLS_components.ElTableColumn, typeof __VLS_components.elTableColumn, ]} */ ;
// @ts-ignore
const __VLS_52 = __VLS_asFunctionalComponent(__VLS_51, new __VLS_51({
    label: "操作",
    width: "120",
}));
const __VLS_53 = __VLS_52({
    label: "操作",
    width: "120",
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
            __VLS_ctx.inspectRecord(row.billNo);
        }
    };
    __VLS_58.slots.default;
    var __VLS_58;
}
var __VLS_54;
var __VLS_26;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            DataToolbar: DataToolbar,
            PageContainer: PageContainer,
            keyword: keyword,
            status: status,
            filters: filters,
            filteredPayments: filteredPayments,
            reconcile: reconcile,
            exportRecords: exportRecords,
            inspectRecord: inspectRecord,
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
