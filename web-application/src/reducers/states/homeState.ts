import Category from "../../model/Category";
import Product from "../../model/Product";
import Vendor from "../../model/Vendor";

export interface HomeState {
    products: Product[];
    currentPage: number;
    pageSize: number;
    sortBy: string[];
    vendors: Vendor[];
    categories: Category[];
    currentVendorId: string;
    currentCategoryId: string;
    currentSearchKey: string;
}

export const initialState: HomeState = {
    products: [],
    currentPage: 0,
    pageSize: 6,
    sortBy: ["id", "asc"],
    vendors: [],
    categories: [],
    currentVendorId: "",
    currentCategoryId: "",
    currentSearchKey: ""
};
