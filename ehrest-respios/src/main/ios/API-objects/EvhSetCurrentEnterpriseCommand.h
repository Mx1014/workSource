//
// EvhSetCurrentEnterpriseCommand.h
// generated at 2016-03-31 20:15:29 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetCurrentEnterpriseCommand
//
@interface EvhSetCurrentEnterpriseCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

