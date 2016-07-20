//
// EvhUpdateContactCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContactCommand
//
@interface EvhUpdateContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* contactId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSNumber* role;

@property(nonatomic, copy) NSNumber* contactGroupId;

@property(nonatomic, copy) NSString* employeeNo;

@property(nonatomic, copy) NSString* sex;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

