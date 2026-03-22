import PageContainer from '@/components/PageContainer.vue';
import PieLegendCard from '@/components/PieLegendCard.vue';
import StatCard from '@/components/StatCard.vue';
import { dashboardStats, parkingChart, rentalChart } from '@/mock/data';
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['dashboard-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['chart-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['dashboard-grid']} */ ;
// CSS variable injection 
// CSS variable injection end 
/** @type {[typeof PageContainer, typeof PageContainer, ]} */ ;
// @ts-ignore
const __VLS_0 = __VLS_asFunctionalComponent(PageContainer, new PageContainer({
    title: "管理员运营看板",
    description: "聚合收费、报修、投诉和资产经营数据，帮助管理人员快速了解园区整体运行状态。",
}));
const __VLS_1 = __VLS_0({
    title: "管理员运营看板",
    description: "聚合收费、报修、投诉和资产经营数据，帮助管理人员快速了解园区整体运行状态。",
}, ...__VLS_functionalComponentArgsRest(__VLS_0));
var __VLS_3 = {};
__VLS_2.slots.default;
{
    const { actions: __VLS_thisSlot } = __VLS_2.slots;
    const __VLS_4 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({
        plain: true,
    }));
    const __VLS_6 = __VLS_5({
        plain: true,
    }, ...__VLS_functionalComponentArgsRest(__VLS_5));
    __VLS_7.slots.default;
    var __VLS_7;
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "dashboard-grid" },
});
for (const [stat] of __VLS_getVForSourceType((__VLS_ctx.dashboardStats))) {
    /** @type {[typeof StatCard, ]} */ ;
    // @ts-ignore
    const __VLS_8 = __VLS_asFunctionalComponent(StatCard, new StatCard({
        key: (stat.id),
        stat: (stat),
    }));
    const __VLS_9 = __VLS_8({
        key: (stat.id),
        stat: (stat),
    }, ...__VLS_functionalComponentArgsRest(__VLS_8));
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "chart-grid" },
});
/** @type {[typeof PieLegendCard, ]} */ ;
// @ts-ignore
const __VLS_11 = __VLS_asFunctionalComponent(PieLegendCard, new PieLegendCard({
    title: "商铺租售情况",
    centerLabel: "商铺租售比率",
    segments: (__VLS_ctx.rentalChart),
}));
const __VLS_12 = __VLS_11({
    title: "商铺租售情况",
    centerLabel: "商铺租售比率",
    segments: (__VLS_ctx.rentalChart),
}, ...__VLS_functionalComponentArgsRest(__VLS_11));
/** @type {[typeof PieLegendCard, ]} */ ;
// @ts-ignore
const __VLS_14 = __VLS_asFunctionalComponent(PieLegendCard, new PieLegendCard({
    title: "车位租售情况",
    centerLabel: "车位租售比率",
    segments: (__VLS_ctx.parkingChart),
}));
const __VLS_15 = __VLS_14({
    title: "车位租售情况",
    centerLabel: "车位租售比率",
    segments: (__VLS_ctx.parkingChart),
}, ...__VLS_functionalComponentArgsRest(__VLS_14));
var __VLS_2;
/** @type {__VLS_StyleScopedClasses['dashboard-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['chart-grid']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            PageContainer: PageContainer,
            PieLegendCard: PieLegendCard,
            StatCard: StatCard,
            dashboardStats: dashboardStats,
            parkingChart: parkingChart,
            rentalChart: rentalChart,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
