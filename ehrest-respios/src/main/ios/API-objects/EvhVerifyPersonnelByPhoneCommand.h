//
// EvhVerifyPersonnelByPhoneCommand.h
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

