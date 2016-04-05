//
// EvhImportDataResponse.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImportDataResponse
//
@interface EvhImportDataResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* totalCount;

@property(nonatomic, copy) NSNumber* failCount;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* logs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

