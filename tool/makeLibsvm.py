
def makeLibsvm():
    # read data file
    readin = open('makeLibsvmResoucse.txt', 'r')
    # write data file
    output = open('makeLibsvmDestination.txt', 'w')
    try:
        the_line = readin.readline()
        while the_line:
            # delete the \n
            the_line = the_line.strip('\n')
            index = 0;
            output_line = ''
            for sub_line in the_line.split(' '):
                # the label col
                if index == 0:
                    output_line = sub_line
                # the features cols
                if sub_line != 'NULL' and index != 0:
                    the_text = ' ' + str(index) + ':' + sub_line
                    output_line = output_line + the_text
                index = index + 1
            output_line = output_line + '\n'
            output.write(output_line)
            the_line = readin.readline()
    finally:
        readin.close()


# Debug运行
if __name__ == '__main__':
    makeLibsvm()

