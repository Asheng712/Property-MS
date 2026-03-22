import { computed, reactive, ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import PageContainer from '@/components/PageContainer.vue';
import { roleRecords } from '@/mock/data';
const activeRoleId = ref(roleRecords[0].id);
const dialogVisible = ref(false);
const editableGroups = ref([]);
const draft = reactive({
    name: '',
    description: '',
});
const activeRole = computed(() => roleRecords.find((item) => item.id === activeRoleId.value) ?? roleRecords[0]);
watch(activeRole, (role) => {
    editableGroups.value = role.permissionGroups.map((group) => ({
        id: group.id,
        title: group.title,
        enabled: true,
        permissions: group.permissions.map((permission) => ({
            label: permission,
            checked: true,
        })),
    }));
}, { immediate: true });
function savePermissions() {
    ElMessage.success(`${activeRole.value.name} 的权限配置已保存`);
}
function createRole() {
    dialogVisible.value = false;
    ElMessage.success(`角色 ${draft.name || '新角色'} 已创建`);
    draft.name = '';
    draft.description = '';
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['role-list__item']} */ ;
/** @type {__VLS_StyleScopedClasses['role-list__item']} */ ;
/** @type {__VLS_StyleScopedClasses['role-list__item']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-panel__header']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-panel__header']} */ ;
/** @type {__VLS_StyleScopedClasses['roles-layout']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-group__grid']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-group__grid']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "角色权限控制 (RBAC)",
    description: "细粒度划分不同操作人员的权限边界，支持角色切换和权限即时保存。",
}));
const __VLS_1 = __VLS_0({
    title: "角色权限控制 (RBAC)",
    description: "细粒度划分不同操作人员的权限边界，支持角色切换和权限即时保存。",
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
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "roles-layout" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card role-list" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "section-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
for (const [role] of __VLS_getVForSourceType((__VLS_ctx.roleRecords))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
        ...{ onClick: (...[$event]) => {
                __VLS_ctx.activeRoleId = role.id;
            } },
        key: (role.id),
        type: "button",
        ...{ class: "role-list__item" },
        ...{ class: ({ active: __VLS_ctx.activeRole.id === role.id }) },
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
    (role.name);
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
    (role.description);
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "surface-card permission-panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "permission-panel__header" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({});
(__VLS_ctx.activeRole.name);
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
(__VLS_ctx.activeRole.locked ? '该角色拥有系统的完整控制权，不可修改核心权限。' : __VLS_ctx.activeRole.description);
const __VLS_12 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
    ...{ 'onClick': {} },
    disabled: (Boolean(__VLS_ctx.activeRole.locked)),
    type: "primary",
}));
const __VLS_14 = __VLS_13({
    ...{ 'onClick': {} },
    disabled: (Boolean(__VLS_ctx.activeRole.locked)),
    type: "primary",
}, ...__VLS_functionalComponentArgsRest(__VLS_13));
let __VLS_16;
let __VLS_17;
let __VLS_18;
const __VLS_19 = {
    onClick: (__VLS_ctx.savePermissions)
};
__VLS_15.slots.default;
var __VLS_15;
for (const [group] of __VLS_getVForSourceType((__VLS_ctx.editableGroups))) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "permission-group" },
        key: (group.id),
    });
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "permission-group__title" },
    });
    const __VLS_20 = {}.ElCheckbox;
    /** @type {[typeof __VLS_components.ElCheckbox, typeof __VLS_components.elCheckbox, typeof __VLS_components.ElCheckbox, typeof __VLS_components.elCheckbox, ]} */ ;
    // @ts-ignore
    const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
        modelValue: (group.enabled),
        disabled: (Boolean(__VLS_ctx.activeRole.locked)),
    }));
    const __VLS_22 = __VLS_21({
        modelValue: (group.enabled),
        disabled: (Boolean(__VLS_ctx.activeRole.locked)),
    }, ...__VLS_functionalComponentArgsRest(__VLS_21));
    __VLS_23.slots.default;
    (group.title);
    var __VLS_23;
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "permission-group__grid" },
    });
    for (const [permission] of __VLS_getVForSourceType((group.permissions))) {
        const __VLS_24 = {}.ElCheckbox;
        /** @type {[typeof __VLS_components.ElCheckbox, typeof __VLS_components.elCheckbox, typeof __VLS_components.ElCheckbox, typeof __VLS_components.elCheckbox, ]} */ ;
        // @ts-ignore
        const __VLS_25 = __VLS_asFunctionalComponent(__VLS_24, new __VLS_24({
            key: (permission.label),
            modelValue: (permission.checked),
            disabled: (Boolean(__VLS_ctx.activeRole.locked) || !group.enabled),
        }));
        const __VLS_26 = __VLS_25({
            key: (permission.label),
            modelValue: (permission.checked),
            disabled: (Boolean(__VLS_ctx.activeRole.locked) || !group.enabled),
        }, ...__VLS_functionalComponentArgsRest(__VLS_25));
        __VLS_27.slots.default;
        (permission.label);
        var __VLS_27;
    }
}
const __VLS_28 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新增角色",
    width: "500px",
}));
const __VLS_30 = __VLS_29({
    modelValue: (__VLS_ctx.dialogVisible),
    title: "新增角色",
    width: "500px",
}, ...__VLS_functionalComponentArgsRest(__VLS_29));
__VLS_31.slots.default;
const __VLS_32 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_33 = __VLS_asFunctionalComponent(__VLS_32, new __VLS_32({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}));
const __VLS_34 = __VLS_33({
    labelPosition: "top",
    ...{ class: "dialog-form" },
}, ...__VLS_functionalComponentArgsRest(__VLS_33));
__VLS_35.slots.default;
const __VLS_36 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    label: "角色名称",
}));
const __VLS_38 = __VLS_37({
    label: "角色名称",
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
__VLS_39.slots.default;
const __VLS_40 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    modelValue: (__VLS_ctx.draft.name),
}));
const __VLS_42 = __VLS_41({
    modelValue: (__VLS_ctx.draft.name),
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
var __VLS_39;
const __VLS_44 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    label: "角色说明",
}));
const __VLS_46 = __VLS_45({
    label: "角色说明",
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
__VLS_47.slots.default;
const __VLS_48 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    modelValue: (__VLS_ctx.draft.description),
    type: "textarea",
    rows: "4",
}));
const __VLS_50 = __VLS_49({
    modelValue: (__VLS_ctx.draft.description),
    type: "textarea",
    rows: "4",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
var __VLS_47;
var __VLS_35;
{
    const { footer: __VLS_thisSlot } = __VLS_31.slots;
    const __VLS_52 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_53 = __VLS_asFunctionalComponent(__VLS_52, new __VLS_52({
        ...{ 'onClick': {} },
    }));
    const __VLS_54 = __VLS_53({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_53));
    let __VLS_56;
    let __VLS_57;
    let __VLS_58;
    const __VLS_59 = {
        onClick: (...[$event]) => {
            __VLS_ctx.dialogVisible = false;
        }
    };
    __VLS_55.slots.default;
    var __VLS_55;
    const __VLS_60 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_61 = __VLS_asFunctionalComponent(__VLS_60, new __VLS_60({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_62 = __VLS_61({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_61));
    let __VLS_64;
    let __VLS_65;
    let __VLS_66;
    const __VLS_67 = {
        onClick: (__VLS_ctx.createRole)
    };
    __VLS_63.slots.default;
    var __VLS_63;
}
var __VLS_31;
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['btn-primary-gradient']} */ ;
/** @type {__VLS_StyleScopedClasses['roles-layout']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['role-list']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['role-list__item']} */ ;
/** @type {__VLS_StyleScopedClasses['surface-card']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-panel__header']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-group']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-group__title']} */ ;
/** @type {__VLS_StyleScopedClasses['permission-group__grid']} */ ;
/** @type {__VLS_StyleScopedClasses['dialog-form']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            roleRecords: roleRecords,
            activeRoleId: activeRoleId,
            dialogVisible: dialogVisible,
            editableGroups: editableGroups,
            draft: draft,
            activeRole: activeRole,
            savePermissions: savePermissions,
            createRole: createRole,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
