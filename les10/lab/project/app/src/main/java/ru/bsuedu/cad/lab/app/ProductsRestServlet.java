package ru.bsuedu.cad.lab.app;

import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/products")
public class ProductsRestServlet extends HttpServlet {
    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.productRepository = context.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        List<Product> products = productRepository.findAll();
        out.print("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            out.print("{\"name\":\"" + escape(p.getName()) + "\"," +
                    "\"category\":\"" + (p.getCategory() != null ? escape(p.getCategory().getName()) : "") + "\"," +
                    "\"stock\":" + p.getStockQuantity() + "}");
            if (i < products.size() - 1) out.print(",");
        }
        out.print("]");
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
} 