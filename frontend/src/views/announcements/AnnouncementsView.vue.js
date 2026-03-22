import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import PageContainer from '@/components/PageContainer.vue';
import { noticeRecords } from '@/mock/data';
const dialogVisible = ref(false);
const draft = reactive({
    title: '',
    audience: '全体业主',
    summary: '',
});
function saveDraft() {
    dialogVisible.value = false;
    ElMessage.success('公告草稿已保存');
}
function publishNotice() {
    dialogVisible.value = false;
    ElMessage.success('公告已发布到目标对象');
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['announcement-card']} */ ;
/** @type {__VLS_StyleScopedClasses['announcement-card']} */ ;
/** @type {__VLS_StyleScopedClasses['announcement-grid']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "定向公告发布",
    description: "支持按楼栋、角色或车主群体定向下发公告，提升通知触达效率。",
}));
const __VLS_1 = __VLS_0({
    title: "定向公告发布",
    description: "支持按楼栋、角色或车主群体定向下发公告，提升通知触达效率。",
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
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "announcement-grid" },
});
for (const [notice] of __VLS_getVForSourceType((__VLS_ctx.noticeRecords))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
        key: (notice.id),
        ...{ class: "surface-card announcement-card" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "announcement-card__status" },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "status-pill" },
        ...{ class: (notice.status === 'published' ? 'success' : 'info') },
    });
    (notice.status === 'published' ? '已发布' : '草稿');
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "muted-text" },
    });
    (notice.publishAt);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
    (notice.title);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
    (notice.summary);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.footer, __VLS_intrinsicElements.footer)({});
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (notice.audience);
    const __VLS_12 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
        link: true,
        type: "primary",
    }));
    const __VLS_14 = __VLS_13({
        link: true,
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_13));
    __VLS_15.slots.default;
    var __VLS_15;
}
const __VLS_16 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_17 = __VLS_asFunctionalComponent(__VLS_16, new __VLS_16({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新建公告",
    width: "560px",
}));
const __VLS_18 = __VLS_17({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新建公告",
    width: "560px",
}, ...__VLS_functionalComponentArgsRest(__VLS_17));
__VLS_19.slots.default;
const __VLS_20 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_22 = __VLS_21({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_21));
__VLS_23.slots.default;
const __VLS_24 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_25 = __VLS_asFunctionalComponent(__VLS_24, new __VLS_24({
    label: "公告标题",
}));
const __VLS_26 = __VLS_25({
    label: "公告标题",
}, ...__VLS_functionalComponentArgsRest(__VLS_25));
__VLS_27.slots.default;
const __VLS_28 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
    modelValue: (__VLS_ctx.draft.title),
}));
const __VLS_30 = __VLS_29({
    modelValue: (__VLS_ctx.draft.title),
}, ...__VLS_functionalComponentArgsRest(__VLS_29));
var __VLS_27;
const __VLS_32 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_33 = __VLS_asFunctionalComponent(__VLS_32, new __VLS_32({
    label: "推送对象",
}));
const __VLS_34 = __VLS_33({
    label: "推送对象",
}, ...__VLS_functionalComponentArgsRest(__VLS_33));
__VLS_35.slots.default;
const __VLS_36 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    modelValue: (__VLS_ctx.draft.audience),
}));
const __VLS_38 = __VLS_37({
    modelValue: (__VLS_ctx.draft.audience),
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
__VLS_39.slots.default;
const __VLS_40 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    label: "全体业主",
    value: "全体业主",
}));
const __VLS_42 = __VLS_41({
    label: "全体业主",
    value: "全体业主",
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
const __VLS_44 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    label: "A区车主",
    value: "A区车主",
}));
const __VLS_46 = __VLS_45({
    label: "A区车主",
    value: "A区车主",
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
const __VLS_48 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    label: "商铺租户",
    value: "商铺租户",
}));
const __VLS_50 = __VLS_49({
    label: "商铺租户",
    value: "商铺租户",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
var __VLS_39;
var __VLS_35;
const __VLS_52 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_53 = __VLS_asFunctionalComponent(__VLS_52, new __VLS_52({
    label: "公告内容",
}));
const __VLS_54 = __VLS_53({
    label: "公告内容",
}, ...__VLS_functionalComponentArgsRest(__VLS_53));
__VLS_55.slots.default;
const __VLS_56 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_57 = __VLS_asFunctionalComponent(__VLS_56, new __VLS_56({
    modelValue: (__VLS_ctx.draft.summary),
    type: "textarea",
    rows: "6",
}));
const __VLS_58 = __VLS_57({
    modelValue: (__VLS_ctx.draft.summary),
    type: "textarea",
    rows: "6",
}, ...__VLS_functionalComponentArgsRest(__VLS_57));
var __VLS_55;
var __VLS_23;
{
    const { footer: __VLS_thisSlot } = __VLS_19.slots;
    const __VLS_60 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_61 = __VLS_asFunctionalComponent(__VLS_60, new __VLS_60({
        ...{ 'onClick': {} },
    }));
    const __VLS_62 = __VLS_61({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_61));
    let __VLS_64;
    let __VLS_65;
    let __VLS_66;
    const __VLS_67 = {
        onClick: (__VLS_ctx.saveDraft)
    };
    __VLS_63.slots.default;
    var __VLS_63;
    const __VLS_68 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_69 = __VLS_asFunctionalComponent(__VLS_68, new __VLS_68({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_70 = __VLS_69({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_69));
    let __VLS_72;
    let __VLS_73;
    let __VLS_74;
    const __VLS_75 = {
        onClick: (__VLS_ctx.publishNotice)
    };
    __VLS_71.slots.default;
    var __VLS_71;
}
var __VLS_19;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['announcement-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['announcement-card']} */ ;
/** @type {__VLS_StyleScopedClasses['announcement-card__status']} */ ;
/** @type {__VLS_StyleScopedClasses['status-pill']} */ ;
/** @type {__VLS_StyleScopedClasses['muted-text']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            noticeRecords: noticeRecords,
            dialogVisible: dialogVisible,
            draft: draft,
            saveDraft: saveDraft,
            publishNotice: publishNotice,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
