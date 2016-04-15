//
// EvhListConfigurationsAdminCommandResponse.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfigurationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfigurationsAdminCommandResponse
//
@interface EvhListConfigurationsAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhConfigurationDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

