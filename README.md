
---

## Main Controller

### 1. Add Currency
**Endpoint:**  
`POST /addCurrency`  
**Description:**  
Saves the given currency to the database.

### 2. Fetch Currencies (Source 1)
**Endpoint:**  
`GET /fetchCurrencies`  
**Description:**  
Retrieves all currency records from Source 1 (CurrencyLayer).

### 3. Fetch Currencies (Source 2)
**Endpoint:**  
`GET /fetchCurrenciesSource2`  
**Description:**  
Retrieves all currency records from Source 2 (Fixer.io).

---

## Currency Controller

### 1. Get All Records by Type
**Endpoint:**  
- `GET /api/currencies/USD`  
- `GET /api/currencies/EUR`  

**Description:**  
Fetches all the records of a specific currency type (USD or EUR).

### 2. Get Last Recorded Currency Rate
**Endpoint:**  
`GET /api/lastRates/{currency}`  
**Path Parameters:**  
- `currency` - The currency type (e.g., `EUR` or `USD`).

**Description:**  
Retrieves the latest recorded exchange rate for the specified currency type.

### 3. Get Records with Pagination
**Endpoints:**  
- `GET /api/currencies/pagination/USD`  
- `GET /api/currencies/pagination/EUR`  

**Query Parameters:**  
- `page` - The page number (starting from 0).  
- `size` - The number of records per page.

**Description:**  
Fetches a paginated list of records for the specified currency type (USD or EUR).

---

## Notes
- Ensure the server is running on the base URL `http://localhost:8081/`.  
- Check the request parameters in the Currency Controller class for proper usage.





