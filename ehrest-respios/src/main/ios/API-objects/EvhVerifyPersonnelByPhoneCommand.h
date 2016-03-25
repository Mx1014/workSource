//
// EvhVerifyPersonnelByPhoneCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPersonnelByPhoneCommand
//
@interface EvhVerifyPersonnelByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

