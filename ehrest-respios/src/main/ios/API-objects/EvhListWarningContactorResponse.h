//
// EvhListWarningContactorResponse.h
// generated at 2016-03-30 10:13:07 
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

