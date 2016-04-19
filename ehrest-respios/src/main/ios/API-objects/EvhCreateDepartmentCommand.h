//
// EvhCreateDepartmentCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDepartmentCommand
//
@interface EvhCreateDepartmentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* departmentName;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* departmentType;

@property(nonatomic, copy) NSNumber* roleId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

