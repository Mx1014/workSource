//
// EvhRentalActionData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalActionData
//
@interface EvhRentalActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* launchPadItemId;

@property(nonatomic, copy) NSNumber* pageType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

