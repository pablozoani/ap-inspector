import axios from "axios";
import { PRODUCTS_URL } from "../constants/urls";
import Product from "../model/Product";

export const getProducts = (
    sortBy: string,
    sortOrder: string,
    pageOffset: number,
    pageSize: number,
    vendorId: string,
    categoryId: string,
    searchKey: string
) => axios.get<Product[]>(
    PRODUCTS_URL + "?" +
    "sortBy=" + sortBy + "&" +
    "sortOrder=" + sortOrder + "&" +
    "page=" + pageOffset + "&" +
    "size=" + pageSize + "&" +
    "vendorId=" + vendorId + "&" +
    "categoryId=" + categoryId + "&" +
    "productNameLike=" + searchKey
);

export const getProductCount = (
    currentCategoryId: string, currentVendorId: string, currentSearchKey: string
) => axios.get<number>(
    PRODUCTS_URL + "/count" + "?" +
    "vendorId=" + currentVendorId + "&" +
    "categoryId=" + currentCategoryId + "&" +
    "productNameLike=" + currentSearchKey
);