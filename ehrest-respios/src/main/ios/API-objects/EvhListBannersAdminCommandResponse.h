//
// EvhListBannersAdminCommandResponse.h
// generated at 2016-03-25 09:26:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBannerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBannersAdminCommandResponse
//
@interface EvhListBannersAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhBannerDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

