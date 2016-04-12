//
// EvhListEnterpriseByPhoneCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseByPhoneCommand
//
@interface EvhListEnterpriseByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

