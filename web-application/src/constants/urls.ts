export const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080"

export const CATEGORIES_URL = `${API_BASE_URL}/api/v1/categories`;
export const PRODUCTS_URL = `${API_BASE_URL}/api/v1/products`;
export const VENDORS_URL = `${API_BASE_URL}/api/v1/vendors`;