import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'

import DataToolbar from '../DataToolbar.vue'
import BrandLogo from '../BrandLogo.vue'
import InfoList from '../InfoList.vue'
import PageContainer from '../PageContainer.vue'
import PanelCard from '../PanelCard.vue'
import PieLegendCard from '../PieLegendCard.vue'
import StatCard from '../StatCard.vue'
import StatusBadge from '../StatusBadge.vue'

const elementStubs = {
  ElIcon: {
    template: '<i><slot /></i>',
  },
  ElInput: {
    props: ['modelValue', 'placeholder'],
    emits: ['update:modelValue'],
    template:
      '<label><span>{{ placeholder }}</span><input :value="modelValue" @input="$emit(\'update:modelValue\', $event.target.value)" /></label>',
  },
  ElSelect: {
    props: ['modelValue', 'placeholder'],
    emits: ['update:modelValue'],
    template:
      '<label><span>{{ placeholder }}</span><select :value="modelValue" @change="$emit(\'update:modelValue\', $event.target.value)"><slot /></select></label>',
  },
  ElOption: {
    props: ['label', 'value'],
    template: '<option :value="value">{{ label }}</option>',
  },
  Search: true,
  InfoFilled: true,
}

describe('core UI components', () => {
  it('renders the full brand logo by default', () => {
    const wrapper = mount(BrandLogo)

    expect(wrapper.text()).toContain('智慧物业管理系统')
    expect(wrapper.text()).toContain('WisdomPM')
    expect(wrapper.find('.brand-logo').classes()).not.toContain('compact')
  })

  it('hides brand text when compact mode is enabled', () => {
    const wrapper = mount(BrandLogo, {
      props: {
        compact: true,
      },
    })

    expect(wrapper.find('.brand-logo').classes()).toContain('compact')
    expect(wrapper.text()).toBe('W')
  })

  it('renders status text with the requested tone class', () => {
    const wrapper = mount(StatusBadge, {
      props: {
        label: '已启用',
        tone: 'success',
      },
    })

    expect(wrapper.text()).toBe('已启用')
    expect(wrapper.classes()).toContain('success')
  })

  it('renders stat values, labels, units, and configured accent styles', () => {
    const wrapper = mount(StatCard, {
      props: {
        stat: {
          id: 'income',
          label: '本月收入',
          value: '12.6万',
          unit: '元',
          color: '#2563eb',
          accent: '#dbeafe',
          icon: 'money',
        },
      },
    })

    expect(wrapper.text()).toContain('12.6万')
    expect(wrapper.text()).toContain('本月收入')
    expect(wrapper.text()).toContain('元')
    expect(wrapper.find('.stat-card__icon').attributes('style')).toContain('background: #dbeafe')
  })

  it('renders every info-list label and value', () => {
    const wrapper = mount(InfoList, {
      props: {
        items: [
          { label: '房号', value: 'A1-101' },
          { label: '面积', value: 120 },
        ],
      },
    })

    expect(wrapper.findAll('.info-list__item')).toHaveLength(2)
    expect(wrapper.text()).toContain('房号')
    expect(wrapper.text()).toContain('A1-101')
    expect(wrapper.text()).toContain('120')
  })

  it('renders page title, description, body, and action slot', () => {
    const wrapper = mount(PageContainer, {
      props: {
        title: '资产管理',
        description: '统一维护楼栋、房屋和商铺',
      },
      slots: {
        actions: '<button>新增资产</button>',
        default: '<main>资产列表</main>',
      },
    })

    expect(wrapper.find('h1').text()).toBe('资产管理')
    expect(wrapper.text()).toContain('统一维护楼栋、房屋和商铺')
    expect(wrapper.text()).toContain('新增资产')
    expect(wrapper.text()).toContain('资产列表')
  })

  it('renders panel header metadata and action slot when provided', () => {
    const wrapper = mount(PanelCard, {
      props: {
        title: '收费概览',
        description: '按月统计收费进度',
      },
      slots: {
        actions: '<button>导出</button>',
        default: '<p>收费数据</p>',
      },
    })

    expect(wrapper.find('.panel-card__header').exists()).toBe(true)
    expect(wrapper.text()).toContain('收费概览')
    expect(wrapper.text()).toContain('按月统计收费进度')
    expect(wrapper.text()).toContain('导出')
    expect(wrapper.text()).toContain('收费数据')
  })

  it('omits the panel header when no title or header slots are provided', () => {
    const wrapper = mount(PanelCard, {
      slots: {
        default: '<p>纯内容区域</p>',
      },
    })

    expect(wrapper.find('.panel-card__header').exists()).toBe(false)
    expect(wrapper.text()).toContain('纯内容区域')
  })

  it('renders pie legend rows and computes the conic-gradient background', () => {
    const wrapper = mount(PieLegendCard, {
      global: {
        stubs: elementStubs,
      },
      props: {
        title: '出租率',
        centerLabel: '100%',
        segments: [
          { label: '已出租', value: 65, color: '#10b981' },
          { label: '空置', value: 35, color: '#3b82f6' },
        ],
      },
    })

    expect(wrapper.text()).toContain('出租率')
    expect(wrapper.text()).toContain('100%')
    expect(wrapper.findAll('.pie-card__legend-item')).toHaveLength(2)
    expect(wrapper.find('.pie-card__circle').attributes('style')).toContain('conic-gradient')
  })

  it('emits keyword and status updates from toolbar controls', async () => {
    const wrapper = mount(DataToolbar, {
      global: {
        stubs: elementStubs,
      },
      props: {
        keyword: '',
        status: '',
        placeholder: '搜索房号',
        selectPlaceholder: '状态',
        filters: [
          { label: '启用', value: 'enabled' },
          { label: '停用', value: 'disabled' },
        ],
      },
      slots: {
        default: '<button>新增</button>',
      },
    })

    await wrapper.find('input').setValue('A1')
    await wrapper.find('select').setValue('enabled')

    expect(wrapper.emitted('update:keyword')?.[0]).toEqual(['A1'])
    expect(wrapper.emitted('update:status')?.[0]).toEqual(['enabled'])
    expect(wrapper.text()).toContain('新增')
  })
})
