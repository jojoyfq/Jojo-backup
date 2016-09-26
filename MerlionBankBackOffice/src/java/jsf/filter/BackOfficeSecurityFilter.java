/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author liyanmeng
 */
@WebFilter(filterName = "BackOfficeSecurityFilter", urlPatterns = {"/CustomerManagement/*", "/DepositManagement/*", "/FixedDepositManagement/*", "/StaffSelfManagement/*", "/StaffVerifyCustomer/*", "/SuperAdminManagement/*", "/TellerManagement/*", "/TransferManagement/*", "/StaffDashboard.xhtml"})
public class BackOfficeSecurityFilter implements Filter{
    
    private FilterConfig filterConfig = null;

    public BackOfficeSecurityFilter() {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String REDIRECT = "?faces-redirect=true";
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpSession httpSession = httpServletRequest.getSession(true);
            String requestServletPath = httpServletRequest.getServletPath();

            if ((!requestServletPath.equals("/staffLogInHome.xhtml")) && (!requestServletPath.equals("/StaffSelfManagement/staffAccountActivationVerifyDetails.xhtml"))
                    && (!requestServletPath.equals("/StaffSelfManagement/resetPassword.xhtml")) && (!requestServletPath.equals("/StaffSelfManagement/resetPasswordVerifyDetails.xhtml"))
                System.out.println("Check Security Filter");

                Boolean isLogin = (httpSession.getAttribute("isLogin") != null) ? ((Boolean) httpSession.getAttribute("isLogin")) : (false);

                if (!isLogin) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    String contextPath = httpServletRequest.getContextPath();
                    res.sendRedirect(contextPath + "/staffLogInHome.xhtml" + REDIRECT);
                }
            }
//            if(!requestServletPath.equals("/LogInHome.xhtml")){
                chain.doFilter(request, response);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
}
