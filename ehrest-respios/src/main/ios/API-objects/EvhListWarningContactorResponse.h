//
// EvhListWarningContactorResponse.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWarningContactorDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWarningContactorResponse
//
@interface EvhListWarningContactorResponse
    : NSObject<EvhJsonSerializable>


// item type EvhWarningContactorDTO*
@property(nonatomic, strong) NSMutableArray* contactors;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

