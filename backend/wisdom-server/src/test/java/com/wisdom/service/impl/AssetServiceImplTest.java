package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.AssetDTO;
import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.entity.House;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.result.PageResult;
import com.wisdom.vo.AssetTreeVO;
import com.wisdom.vo.AssetVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    @Test
    void getAssetTreeShouldBuildTreeStructure() {
        House root = new House();
        root.setId(1L);
        root.setParentId(0L);
        root.setName("小区A");
        root.setType("BUILDING");

        House child = new House();
        child.setId(2L);
        child.setParentId(1L);
        child.setName("1栋");
        child.setType("UNIT");

        when(assetMapper.selectAllAssets()).thenReturn(List.of(root, child));

        List<AssetTreeVO> tree = assetService.getAssetTree();

        assertEquals(1, tree.size());
        assertEquals("小区A", tree.get(0).getName());
        assertEquals(1, tree.get(0).getChildren().size());
        assertEquals("1栋", tree.get(0).getChildren().get(0).getName());
    }

    @Test
    void getAssetTreeShouldReturnEmptyWhenNoAssets() {
        when(assetMapper.selectAllAssets()).thenReturn(List.of());

        List<AssetTreeVO> tree = assetService.getAssetTree();

        assertEquals(0, tree.size());
    }

    @Test
    void getByIdShouldReturnAssetWhenExists() {
        House house = new House();
        house.setId(1L);
        house.setName("1栋101");
        house.setType("RESIDENTIAL");
        house.setArea(new BigDecimal("120.50"));

        when(assetMapper.selectById(1L)).thenReturn(house);

        AssetVO result = assetService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1栋101", result.getName());
    }

    @Test
    void getByIdShouldReturnNullWhenNotFound() {
        when(assetMapper.selectById(999L)).thenReturn(null);

        AssetVO result = assetService.getById(999L);

        assertNull(result);
    }

    @Test
    void getAssetListShouldReturnPagedDataWithFilters() {
        AssetPageQueryDTO dto = new AssetPageQueryDTO();
        dto.setPage(2);
        dto.setPageSize(5);
        dto.setName("商铺");
        dto.setType("SHOP");
        dto.setStatus("RENTING");

        House house = new House();
        house.setId(3L);
        house.setName("商铺A");
        house.setType("SHOP");
        house.setStatus("RENTING");

        Page<House> page = new Page<>(2, 5);
        page.setRecords(List.of(house));
        page.setTotal(1);

        when(assetMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<AssetVO> result = assetService.getAssetList(dto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("商铺A", result.getRecords().get(0).getName());

        ArgumentCaptor<Page<House>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(assetMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(2, pageCaptor.getValue().getCurrent());
        assertEquals(5, pageCaptor.getValue().getSize());
    }

    @Test
    void getAssetListShouldUseDefaultsWhenNoPagination() {
        AssetPageQueryDTO dto = new AssetPageQueryDTO();
        Page<House> page = new Page<>(1, 10);
        page.setRecords(List.of());

        when(assetMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        assetService.getAssetList(dto);

        ArgumentCaptor<Page<House>> captor = ArgumentCaptor.forClass(Page.class);
        verify(assetMapper).selectPage(captor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(1, captor.getValue().getCurrent());
        assertEquals(10, captor.getValue().getSize());
    }

    @Test
    void saveOrUpdateAssetShouldInsertWhenIdIsNull() {
        AssetDTO dto = new AssetDTO();
        dto.setName("新商铺");
        dto.setType("SHOP");
        dto.setArea(new BigDecimal("200"));

        assetService.saveOrUpdateAsset(dto);

        ArgumentCaptor<House> captor = ArgumentCaptor.forClass(House.class);
        verify(assetMapper, times(1)).insert(captor.capture());
        verify(assetMapper, never()).updateById(any(House.class));
        assertEquals("新商铺", captor.getValue().getName());
    }

    @Test
    void saveOrUpdateAssetShouldUpdateWhenHouseExists() {
        AssetDTO dto = new AssetDTO();
        dto.setId(5L);
        dto.setName("已存在商铺");

        House existingHouse = new House();
        existingHouse.setId(5L);
        existingHouse.setName("旧名称");

        when(assetMapper.selectById(5L)).thenReturn(existingHouse);

        assetService.saveOrUpdateAsset(dto);

        verify(assetMapper, never()).insert(any(House.class));
        verify(assetMapper, times(1)).updateById(any(House.class));
    }

    @Test
    void saveOrUpdateAssetShouldInsertWhenHouseNotFound() {
        AssetDTO dto = new AssetDTO();
        dto.setId(999L);
        dto.setName("新商铺");

        when(assetMapper.selectById(999L)).thenReturn(null);

        assetService.saveOrUpdateAsset(dto);

        verify(assetMapper, times(1)).insert(any(House.class));
        verify(assetMapper, never()).updateById(any(House.class));
    }

    @Test
    void saveOrUpdateAssetShouldClearEmptyStrings() {
        AssetDTO dto = new AssetDTO();
        dto.setOwnerName("  ");
        dto.setOwnerPhone("");

        assetService.saveOrUpdateAsset(dto);

        ArgumentCaptor<House> captor = ArgumentCaptor.forClass(House.class);
        verify(assetMapper).insert(captor.capture());
        assertNull(captor.getValue().getOwnerName());
        assertNull(captor.getValue().getOwnerPhone());
    }

    @Test
    void deleteAssetShouldCallMapper() {
        assetService.deleteAsset(10L);

        verify(assetMapper, times(1)).deleteById(10L);
    }

    @Test
    void getOwnedHousesShouldReturnCurrentUserHouses() {
        BaseContext.setCurrentId(1L);

        House house = new House();
        house.setId(10L);
        house.setName("自有住宅");
        house.setType("RESIDENTIAL");

        when(assetMapper.selectHousesByOwnerId(1L)).thenReturn(List.of(house));

        List<AssetVO> result = assetService.getOwnedHouses();

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals("自有住宅", result.get(0).getName());

        BaseContext.removeCurrentId();
    }

    @Test
    void getOwnedHousesShouldReturnEmptyWhenNoHouses() {
        BaseContext.setCurrentId(99L);

        when(assetMapper.selectHousesByOwnerId(99L)).thenReturn(List.of());

        List<AssetVO> result = assetService.getOwnedHouses();

        assertEquals(0, result.size());

        BaseContext.removeCurrentId();
    }
}
