//
// EvhWebMenuPrivilegeDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWebMenuPrivilegeDTO
//
@interface EvhWebMenuPrivilegeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* menuId;

@property(nonatomic, copy) NSNumber* privilegeId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* showFlag;

@property(nonatomic, copy) NSString* discription;

@property(nonatomic, copy) NSNumber* sortNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

