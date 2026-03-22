import { Fold, FullScreen, Menu, Search, Setting } from '@element-plus/icons-vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import BrandLogo from '@/components/BrandLogo.vue';
import { navigationItems } from '@/mock/data';
import { useAppStore } from '@/stores/app';
const appStore = useAppStore();
const route = useRoute();
const currentTitle = computed(() => String(route.meta.title ?? '智慧物业管理系统'));
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['el-menu-item']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__breadcrumb']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar']} */ ;
/** @type {__VLS_StyleScopedClasses['mobile-only']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__breadcrumb']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__right']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__user']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "layout-shell" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.aside, __VLS_intrinsicElements.aside)({
    ...{ class: "sidebar" },
    ...{ style: ({ width: __VLS_ctx.appStore.sidebarWidth }) },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "sidebar__brand" },
});
/** @type {[typeof BrandLogo, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(BrandLogo, new BrandLogo({
    compact: (__VLS_ctx.appStore.sidebarCollapsed),
}));
const __VLS_1 = __VLS_0({
    compact: (__VLS_ctx.appStore.sidebarCollapsed),
}, ...__VLS_functionalComponentArgsRest(__VLS_0));
const __VLS_3 = {}.ElScrollbar;
/** @type {[typeof __VLS_components.ElScrollbar, typeof __VLS_components.elScrollbar, typeof __VLS_components.ElScrollbar, typeof __VLS_components.elScrollbar, ]} */ ;
// @ts-ignore
const __VLS_4 = __VLS_asFunctionalComponent(__VLS_3, new __VLS_3({}));
const __VLS_5 = __VLS_4({}, ...__VLS_functionalComponentArgsRest(__VLS_4));
__VLS_6.slots.default;
const __VLS_7 = {}.ElMenu;
/** @type {[typeof __VLS_components.ElMenu, typeof __VLS_components.elMenu, typeof __VLS_components.ElMenu, typeof __VLS_components.elMenu, ]} */ ;
// @ts-ignore
const __VLS_8 = __VLS_asFunctionalComponent(__VLS_7, new __VLS_7({
    collapse: (__VLS_ctx.appStore.sidebarCollapsed),
    defaultActive: (__VLS_ctx.route.path),
    backgroundColor: "#0f172a",
    textColor: "#8ca0bd",
    activeTextColor: "#ffffff",
    router: true,
}));
const __VLS_9 = __VLS_8({
    collapse: (__VLS_ctx.appStore.sidebarCollapsed),
    defaultActive: (__VLS_ctx.route.path),
    backgroundColor: "#0f172a",
    textColor: "#8ca0bd",
    activeTextColor: "#ffffff",
    router: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_8));
__VLS_10.slots.default;
for (const [item] of __VLS_getVForSourceType((__VLS_ctx.navigationItems))) {
    const __VLS_11 = {}.ElMenuItem;
    /** @type {[typeof __VLS_components.ElMenuItem, typeof __VLS_components.elMenuItem, typeof __VLS_components.ElMenuItem, typeof __VLS_components.elMenuItem, ]} */ ;
    // @ts-ignore
    const __VLS_12 = __VLS_asFunctionalComponent(__VLS_11, new __VLS_11({
        key: (item.path),
        index: (item.path),
    }));
    const __VLS_13 = __VLS_12({
        key: (item.path),
        index: (item.path),
    }, ...__VLS_functionalComponentArgsRest(__VLS_12));
    __VLS_14.slots.default;
    const __VLS_15 = {}.ElIcon;
    /** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
    // @ts-ignore
    const __VLS_16 = __VLS_asFunctionalComponent(__VLS_15, new __VLS_15({}));
    const __VLS_17 = __VLS_16({}, ...__VLS_functionalComponentArgsRest(__VLS_16));
    __VLS_18.slots.default;
    const __VLS_19 = ((item.icon));
    // @ts-ignore
    const __VLS_20 = __VLS_asFunctionalComponent(__VLS_19, new __VLS_19({}));
    const __VLS_21 = __VLS_20({}, ...__VLS_functionalComponentArgsRest(__VLS_20));
    var __VLS_18;
    {
        const { title: __VLS_thisSlot } = __VLS_14.slots;
        (item.label);
    }
    var __VLS_14;
}
var __VLS_10;
var __VLS_6;
const __VLS_23 = {}.ElDrawer;
/** @type {[typeof __VLS_components.ElDrawer, typeof __VLS_components.elDrawer, typeof __VLS_components.ElDrawer, typeof __VLS_components.elDrawer, ]} */ ;
// @ts-ignore
const __VLS_24 = __VLS_asFunctionalComponent(__VLS_23, new __VLS_23({
    ...{ 'onClose': {} },
    modelValue: (__VLS_ctx.appStore.sidebarDrawerOpen),
    direction: "ltr",
    size: "260px",
    withHeader: (false),
    ...{ class: "mobile-sidebar" },
}));
const __VLS_25 = __VLS_24({
    ...{ 'onClose': {} },
    modelValue: (__VLS_ctx.appStore.sidebarDrawerOpen),
    direction: "ltr",
    size: "260px",
    withHeader: (false),
    ...{ class: "mobile-sidebar" },
}, ...__VLS_functionalComponentArgsRest(__VLS_24));
let __VLS_27;
let __VLS_28;
let __VLS_29;
const __VLS_30 = {
    onClose: (__VLS_ctx.appStore.closeSidebarDrawer)
};
__VLS_26.slots.default;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "mobile-sidebar__inner" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "sidebar__brand sidebar__brand--mobile" },
});
/** @type {[typeof BrandLogo, ]} */ ;
// @ts-ignore
const __VLS_31 = __VLS_asFunctionalComponent(BrandLogo, new BrandLogo({}));
const __VLS_32 = __VLS_31({}, ...__VLS_functionalComponentArgsRest(__VLS_31));
const __VLS_34 = {}.ElMenu;
/** @type {[typeof __VLS_components.ElMenu, typeof __VLS_components.elMenu, typeof __VLS_components.ElMenu, typeof __VLS_components.elMenu, ]} */ ;
// @ts-ignore
const __VLS_35 = __VLS_asFunctionalComponent(__VLS_34, new __VLS_34({
    ...{ 'onSelect': {} },
    defaultActive: (__VLS_ctx.route.path),
    router: true,
}));
const __VLS_36 = __VLS_35({
    ...{ 'onSelect': {} },
    defaultActive: (__VLS_ctx.route.path),
    router: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_35));
let __VLS_38;
let __VLS_39;
let __VLS_40;
const __VLS_41 = {
    onSelect: (__VLS_ctx.appStore.closeSidebarDrawer)
};
__VLS_37.slots.default;
for (const [item] of __VLS_getVForSourceType((__VLS_ctx.navigationItems))) {
    const __VLS_42 = {}.ElMenuItem;
    /** @type {[typeof __VLS_components.ElMenuItem, typeof __VLS_components.elMenuItem, typeof __VLS_components.ElMenuItem, typeof __VLS_components.elMenuItem, ]} */ ;
    // @ts-ignore
    const __VLS_43 = __VLS_asFunctionalComponent(__VLS_42, new __VLS_42({
        key: (item.path),
        index: (item.path),
    }));
    const __VLS_44 = __VLS_43({
        key: (item.path),
        index: (item.path),
    }, ...__VLS_functionalComponentArgsRest(__VLS_43));
    __VLS_45.slots.default;
    const __VLS_46 = {}.ElIcon;
    /** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
    // @ts-ignore
    const __VLS_47 = __VLS_asFunctionalComponent(__VLS_46, new __VLS_46({}));
    const __VLS_48 = __VLS_47({}, ...__VLS_functionalComponentArgsRest(__VLS_47));
    __VLS_49.slots.default;
    const __VLS_50 = ((item.icon));
    // @ts-ignore
    const __VLS_51 = __VLS_asFunctionalComponent(__VLS_50, new __VLS_50({}));
    const __VLS_52 = __VLS_51({}, ...__VLS_functionalComponentArgsRest(__VLS_51));
    var __VLS_49;
    {
        const { title: __VLS_thisSlot } = __VLS_45.slots;
        (item.label);
    }
    var __VLS_45;
}
var __VLS_37;
var __VLS_26;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "layout-main" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.header, __VLS_intrinsicElements.header)({
    ...{ class: "topbar glass-card" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "topbar__left" },
});
const __VLS_54 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_55 = __VLS_asFunctionalComponent(__VLS_54, new __VLS_54({
    ...{ 'onClick': {} },
    circle: true,
    plain: true,
    ...{ class: "mobile-only" },
}));
const __VLS_56 = __VLS_55({
    ...{ 'onClick': {} },
    circle: true,
    plain: true,
    ...{ class: "mobile-only" },
}, ...__VLS_functionalComponentArgsRest(__VLS_55));
let __VLS_58;
let __VLS_59;
let __VLS_60;
const __VLS_61 = {
    onClick: (__VLS_ctx.appStore.openSidebarDrawer)
};
__VLS_57.slots.default;
const __VLS_62 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_63 = __VLS_asFunctionalComponent(__VLS_62, new __VLS_62({}));
const __VLS_64 = __VLS_63({}, ...__VLS_functionalComponentArgsRest(__VLS_63));
__VLS_65.slots.default;
const __VLS_66 = {}.Menu;
/** @type {[typeof __VLS_components.Menu, ]} */ ;
// @ts-ignore
const __VLS_67 = __VLS_asFunctionalComponent(__VLS_66, new __VLS_66({}));
const __VLS_68 = __VLS_67({}, ...__VLS_functionalComponentArgsRest(__VLS_67));
var __VLS_65;
var __VLS_57;
const __VLS_70 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_71 = __VLS_asFunctionalComponent(__VLS_70, new __VLS_70({
    ...{ 'onClick': {} },
    circle: true,
    plain: true,
    ...{ class: "desktop-only" },
}));
const __VLS_72 = __VLS_71({
    ...{ 'onClick': {} },
    circle: true,
    plain: true,
    ...{ class: "desktop-only" },
}, ...__VLS_functionalComponentArgsRest(__VLS_71));
let __VLS_74;
let __VLS_75;
let __VLS_76;
const __VLS_77 = {
    onClick: (__VLS_ctx.appStore.toggleSidebar)
};
__VLS_73.slots.default;
const __VLS_78 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_79 = __VLS_asFunctionalComponent(__VLS_78, new __VLS_78({}));
const __VLS_80 = __VLS_79({}, ...__VLS_functionalComponentArgsRest(__VLS_79));
__VLS_81.slots.default;
const __VLS_82 = {}.Fold;
/** @type {[typeof __VLS_components.Fold, ]} */ ;
// @ts-ignore
const __VLS_83 = __VLS_asFunctionalComponent(__VLS_82, new __VLS_82({}));
const __VLS_84 = __VLS_83({}, ...__VLS_functionalComponentArgsRest(__VLS_83));
var __VLS_81;
var __VLS_73;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "topbar__breadcrumb" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.strong, __VLS_intrinsicElements.strong)({});
(__VLS_ctx.currentTitle);
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "topbar__right" },
});
const __VLS_86 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_87 = __VLS_asFunctionalComponent(__VLS_86, new __VLS_86({
    circle: true,
    plain: true,
}));
const __VLS_88 = __VLS_87({
    circle: true,
    plain: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_87));
__VLS_89.slots.default;
const __VLS_90 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_91 = __VLS_asFunctionalComponent(__VLS_90, new __VLS_90({}));
const __VLS_92 = __VLS_91({}, ...__VLS_functionalComponentArgsRest(__VLS_91));
__VLS_93.slots.default;
const __VLS_94 = {}.Search;
/** @type {[typeof __VLS_components.Search, ]} */ ;
// @ts-ignore
const __VLS_95 = __VLS_asFunctionalComponent(__VLS_94, new __VLS_94({}));
const __VLS_96 = __VLS_95({}, ...__VLS_functionalComponentArgsRest(__VLS_95));
var __VLS_93;
var __VLS_89;
const __VLS_98 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_99 = __VLS_asFunctionalComponent(__VLS_98, new __VLS_98({
    circle: true,
    plain: true,
}));
const __VLS_100 = __VLS_99({
    circle: true,
    plain: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_99));
__VLS_101.slots.default;
const __VLS_102 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_103 = __VLS_asFunctionalComponent(__VLS_102, new __VLS_102({}));
const __VLS_104 = __VLS_103({}, ...__VLS_functionalComponentArgsRest(__VLS_103));
__VLS_105.slots.default;
const __VLS_106 = {}.FullScreen;
/** @type {[typeof __VLS_components.FullScreen, ]} */ ;
// @ts-ignore
const __VLS_107 = __VLS_asFunctionalComponent(__VLS_106, new __VLS_106({}));
const __VLS_108 = __VLS_107({}, ...__VLS_functionalComponentArgsRest(__VLS_107));
var __VLS_105;
var __VLS_101;
const __VLS_110 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_111 = __VLS_asFunctionalComponent(__VLS_110, new __VLS_110({
    circle: true,
    plain: true,
}));
const __VLS_112 = __VLS_111({
    circle: true,
    plain: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_111));
__VLS_113.slots.default;
const __VLS_114 = {}.ElIcon;
/** @type {[typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, typeof __VLS_components.ElIcon, typeof __VLS_components.elIcon, ]} */ ;
// @ts-ignore
const __VLS_115 = __VLS_asFunctionalComponent(__VLS_114, new __VLS_114({}));
const __VLS_116 = __VLS_115({}, ...__VLS_functionalComponentArgsRest(__VLS_115));
__VLS_117.slots.default;
const __VLS_118 = {}.Setting;
/** @type {[typeof __VLS_components.Setting, ]} */ ;
// @ts-ignore
const __VLS_119 = __VLS_asFunctionalComponent(__VLS_118, new __VLS_118({}));
const __VLS_120 = __VLS_119({}, ...__VLS_functionalComponentArgsRest(__VLS_119));
var __VLS_117;
var __VLS_113;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "topbar__user" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "avatar" },
});
(__VLS_ctx.appStore.currentUser.slice(0, 1));
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
(__VLS_ctx.appStore.currentUser);
__VLS_asFunctionalElement(__VLS_intrinsicElements.main, __VLS_intrinsicElements.main)({
    ...{ class: "main-content" },
});
const __VLS_122 = {}.RouterView;
/** @type {[typeof __VLS_components.RouterView, typeof __VLS_components.routerView, ]} */ ;
// @ts-ignore
const __VLS_123 = __VLS_asFunctionalComponent(__VLS_122, new __VLS_122({}));
const __VLS_124 = __VLS_123({}, ...__VLS_functionalComponentArgsRest(__VLS_123));
/** @type {__VLS_StyleScopedClasses['layout-shell']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar__brand']} */ ;
/** @type {__VLS_StyleScopedClasses['mobile-sidebar']} */ ;
/** @type {__VLS_StyleScopedClasses['mobile-sidebar__inner']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar__brand']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar__brand--mobile']} */ ;
/** @type {__VLS_StyleScopedClasses['layout-main']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar']} */ ;
/** @type {__VLS_StyleScopedClasses['glass-card']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__left']} */ ;
/** @type {__VLS_StyleScopedClasses['mobile-only']} */ ;
/** @type {__VLS_StyleScopedClasses['desktop-only']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__breadcrumb']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__right']} */ ;
/** @type {__VLS_StyleScopedClasses['topbar__user']} */ ;
/** @type {__VLS_StyleScopedClasses['avatar']} */ ;
/** @type {__VLS_StyleScopedClasses['main-content']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            Fold: Fold,
            FullScreen: FullScreen,
            Menu: Menu,
            Search: Search,
            Setting: Setting,
            BrandLogo: BrandLogo,
            navigationItems: navigationItems,
            appStore: appStore,
            route: route,
            currentTitle: currentTitle,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
