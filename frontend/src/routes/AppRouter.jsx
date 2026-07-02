// 전체 라우트 정의. 도메인별 페이지를 경로에 매핑한다
import { Routes, Route } from 'react-router-dom'
import LoginPage from '../domains/auth/pages/LoginPage'
import OAuth2RedirectPage from '../domains/auth/pages/OAuth2RedirectPage'
import MerchantHomePage from '../domains/merchant/pages/MerchantHomePage'
import SupplierHomePage from '../domains/supplier/pages/SupplierHomePage'
import ItemListPage from '../domains/item/pages/ItemListPage'
import InventoryDashboardPage from '../domains/inventory/pages/InventoryDashboardPage'
import OrderListPage from '../domains/order/pages/OrderListPage'

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<MerchantHomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/oauth2/redirect" element={<OAuth2RedirectPage />} />
      <Route path="/supplier" element={<SupplierHomePage />} />
      <Route path="/items" element={<ItemListPage />} />
      <Route path="/inventory" element={<InventoryDashboardPage />} />
      <Route path="/orders" element={<OrderListPage />} />
    </Routes>
  )
}
