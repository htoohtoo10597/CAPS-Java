class UpdateButton extends React.Component {
	
		constructor(props){
		super(props)
	}
	
	render(){
		return React.createElement("button", {className : "btn btn-primary mr-1", onClick: () => {window.location = `/admin/update-student/${this.props.studId}`} }, "Update")
	}
}

class DeleteButton extends React.Component {
	
		constructor(props){
		super(props)
	}
	
	render(){
		return React.createElement("button", {className :"btn btn-danger mr-1", onClick: () => {window.location = `/admin/delete-student/${this.props.studId}`}}, "Delete")
	}
}


class StudentRow extends React.Component {
	
	constructor(props){
		super(props)
	}
	
	render(){
		const firstNameCell = React.createElement("td", {}, this.props.firstName)
		const lastNameCell = React.createElement("td", {}, this.props.lastName)
		const emailCell = React.createElement("td", {}, this.props.email)
		
		const updateBtn = React.createElement(UpdateButton, {studId :this.props.id}, null)
		const deleteBtn = React.createElement(DeleteButton, {studId: this.props.id}, null)
		
		const actionsCell = React.createElement("td", {}, [updateBtn, deleteBtn])
		
		return React.createElement("tr", {}, [firstNameCell, lastNameCell, emailCell, actionsCell]);
	}
}



class StudentTable extends React.Component {
	
	constructor(props){
		super(props)
	}
	
	render(){
		const rows = this.props.listStudents.map((x,i) => React.createElement(StudentRow, {...x,key:i}, null))
		
		return React.createElement("tbody", {}, rows)
	}
	
}


const root = ReactDOM.createRoot(document.getElementById("react-table"))


class PageButtonRow extends React.Component {
	
	constructor(props){
		super(props)
	}
	
	render(){
		const buttonArray = [];
		
		for(let i = 0; i < this.props.totalPages; i++){
			buttonArray.push(React.createElement("button", {className: "btn btn-outline-primary mr-1" , onClick: () => this.props.changePage(i+1)}, i+1));
		}

		return React.createElement("div", {}, buttonArray);
	}
	
}

function goToUpdatePage(id){
	
}

function changePage(num){
	fetch(`/api/managestudents/page/${num}`)
		.then(data => data.json())
		.then(function(x){
		
		const tableHeaderCells = ["Student First Name", "Student Last Name", "Email", "Actions"]
			.map(x => React.createElement("td", {}, x));
		
		const tableHeaderRow = React.createElement("tr", {}, tableHeaderCells)	;
		const tableHeader = React.createElement("thead", {className : "thead-light font-weight-bold"}, tableHeaderRow);
		
		for(let i = 0; i < x.listStudents.length; i++){
			console.log(x.listStudents[i].id)
		}
		
		const tableBody = React.createElement(StudentTable, {listStudents : x.listStudents}, null);
		
		const table = React.createElement("table", {className : "table table-striped table-form lists"}, [tableHeader, tableBody]);
		
		const buttonRow = React.createElement(PageButtonRow, 
			{totalPages: x.totalPages, changePage:changePage}, null);
		
		const appContainer = React.createElement("div", {}, [table, buttonRow]);
		
		root.render(appContainer);
	}).catch(console.log)
	
}

changePage(1)
	


