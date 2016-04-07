//
// EvhCreateContactByPhoneCommand.h
// generated at 2016-04-07 15:16:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateContactByPhoneCommand
//
@interface EvhCreateContactByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

