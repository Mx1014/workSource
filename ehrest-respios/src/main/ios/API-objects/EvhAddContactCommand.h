//
// EvhAddContactCommand.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddContactCommand
//
@interface EvhAddContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* employeeNo;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* sex;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* role;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

