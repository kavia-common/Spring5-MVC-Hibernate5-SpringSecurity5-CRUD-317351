<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<div class="row">
	<div class="col-md-12">
		<ul class="breadcrumb">
			<li><a href="${contextRoot}/home">Home</a></li>
			<li class="active">Users List</li>
		</ul>
		<div>
			<table id="userTable" class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>Sl. No.</th>
						<th>Username</th>
						<th>Gender</th>
						<th>City</th>
						<th>History</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="count" value="0" scope="page" />
					<c:forEach items="${list}" var="user">
						<c:set var="count" value="${count + 1}" scope="page" />
						<tr>
							<td>${count}</td>
							<td><a href="${contextRoot}/webrst/editUser?id=${user.id}">${user.name}</a></td>
							<td>${user.gender}</td>
							<td>${user.city}</td>
							<td><a
								href="${contextRoot}/webrst/ticket?userId=${user.id}"
								data-toggle="tooltip" title="User History"><i
									class="fa fa-history" aria-hidden="true"></i></a></td>
							<td>
								<a
									href="${contextRoot}/webrst/editUser?id=${user.id}"
									data-toggle="tooltip" title="Edit"><i
										class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
								|
								<form action="${contextRoot}/webrst/deleteUser" method="post" style="display:inline;">
									<security:csrfInput />
									<input type="hidden" name="id" value="${user.id}" />
									<button type="submit" class="btn btn-link p-0" data-toggle="tooltip" title="Delete"
										style="vertical-align: baseline;">
										<i class="fa fa-trash" aria-hidden="true"></i>
									</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
$(document).ready(function() {
    $('#userTable').DataTable();
});
</script>
