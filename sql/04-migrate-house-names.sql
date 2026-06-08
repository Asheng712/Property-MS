-- ============================================
-- 智慧物业管理系统 — 资产名称规范化迁移
-- 将子资产名称统一加上所属楼栋前缀
-- 例如: "101商铺" -> "A101商铺" (A栋综合楼)
-- 适用: 已有数据库升级 (init 脚本仅对首次部署生效)
-- ============================================

UPDATE bus_house SET name = 'A101商铺'  WHERE id = 2;
UPDATE bus_house SET name = 'A102商铺'  WHERE id = 3;
UPDATE bus_house SET name = 'A201住宅'  WHERE id = 4;
UPDATE bus_house SET name = 'A301住宅'  WHERE id = 8;
UPDATE bus_house SET name = 'A302住宅'  WHERE id = 9;
UPDATE bus_house SET name = 'A401住宅'  WHERE id = 10;
UPDATE bus_house SET name = 'B101办公楼' WHERE id = 6;
UPDATE bus_house SET name = 'B202住宅'  WHERE id = 7;
UPDATE bus_house SET name = 'B103商铺'  WHERE id = 11;
UPDATE bus_house SET name = 'B201办公室' WHERE id = 12;
UPDATE bus_house SET name = 'C101住宅'  WHERE id = 14;
UPDATE bus_house SET name = 'C102住宅'  WHERE id = 15;
UPDATE bus_house SET name = 'C201住宅'  WHERE id = 16;
UPDATE bus_house SET name = 'C301住宅'  WHERE id = 17;
UPDATE bus_house SET name = 'D101商铺'  WHERE id = 19;
UPDATE bus_house SET name = 'D102商铺'  WHERE id = 20;
