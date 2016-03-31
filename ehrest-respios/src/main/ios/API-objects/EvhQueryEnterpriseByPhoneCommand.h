//
// EvhQueryEnterpriseByPhoneCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryEnterpriseByPhoneCommand
//
@interface EvhQueryEnterpriseByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

